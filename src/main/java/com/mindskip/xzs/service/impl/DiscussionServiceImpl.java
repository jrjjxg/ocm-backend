package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.Discussion;
import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.repository.DiscussionMapper;
import com.mindskip.xzs.repository.CourseStudentMapper;
import com.mindskip.xzs.repository.CourseTeacherMapper;
import com.mindskip.xzs.repository.UserMapper;
import com.mindskip.xzs.service.DiscussionService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.viewmodel.student.discussion.DiscussionRequestVM;
import com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionMapper discussionMapper;
    private final UserMapper userMapper;
    private final CourseStudentMapper courseStudentMapper;
    private final TeacherCourseService teacherCourseService;

    @Autowired
    public DiscussionServiceImpl(DiscussionMapper discussionMapper,
            UserMapper userMapper,
            CourseStudentMapper courseStudentMapper,
            TeacherCourseService teacherCourseService) {
        this.discussionMapper = discussionMapper;
        this.userMapper = userMapper;
        this.courseStudentMapper = courseStudentMapper;
        this.teacherCourseService = teacherCourseService;
    }

    @Override
    @Transactional
    public Long createDiscussion(DiscussionRequestVM requestVM, Integer creatorId, Integer creatorType) {
        // 验证用户对课程的权限
        if (!validateCourseAccess(requestVM.getCourseId(), creatorId, creatorType)) {
            throw new RuntimeException("没有权限访问该课程");
        }

        Discussion discussion = new Discussion();
        discussion.setTitle(requestVM.getTitle());
        discussion.setContent(requestVM.getContent());
        discussion.setCourseId(requestVM.getCourseId());
        discussion.setCreatorId(creatorId);
        discussion.setCreatorType(creatorType);
        discussion.setViewCount(0);
        discussion.setReplyCount(0);
        discussion.setIsTop(false);
        discussion.setIsEssence(false);
        discussion.setCreateTime(new Date());
        discussion.setUpdateTime(new Date());

        discussionMapper.insertSelective(discussion);
        return discussion.getId();
    }

    @Override
    @Transactional
    public Long createTeacherDiscussion(com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionRequestVM requestVM,
            Integer creatorId) {
        // 验证教师对课程的权限
        if (!teacherCourseService.validateTeacherCourse(creatorId, requestVM.getCourseId())) {
            throw new RuntimeException("没有权限访问该课程");
        }

        Discussion discussion = new Discussion();
        discussion.setTitle(requestVM.getTitle());
        discussion.setContent(requestVM.getContent());
        discussion.setCourseId(requestVM.getCourseId());
        discussion.setCreatorId(creatorId);
        discussion.setCreatorType(2); // 2表示教师
        discussion.setViewCount(0);
        discussion.setReplyCount(0);
        discussion.setIsTop(requestVM.getIsTop() != null ? requestVM.getIsTop() : false);
        discussion.setIsEssence(requestVM.getIsEssence() != null ? requestVM.getIsEssence() : false);
        discussion.setCreateTime(new Date());
        discussion.setUpdateTime(new Date());

        discussionMapper.insertSelective(discussion);
        return discussion.getId();
    }

    @Override
    public DiscussionResponseVM getDiscussionById(Long id) {
        Discussion discussion = discussionMapper.selectByPrimaryKey(id);
        if (discussion == null) {
            return null;
        }

        // 增加浏览次数
        discussionMapper.increaseViewCount(id);

        return convertToResponseVM(discussion);
    }

    @Override
    public List<DiscussionResponseVM> getDiscussionsByCourseId(Long courseId, Integer pageIndex, Integer pageSize) {
        Integer offset = (pageIndex - 1) * pageSize;
        List<Discussion> discussions = discussionMapper.selectByCourseId(courseId, offset, pageSize);

        List<DiscussionResponseVM> responseVMs = new ArrayList<>();
        for (Discussion discussion : discussions) {
            responseVMs.add(convertToResponseVM(discussion));
        }

        return responseVMs;
    }

    @Override
    public int getDiscussionCountByCourseId(Long courseId) {
        return discussionMapper.countByCourseId(courseId);
    }

    @Override
    @Transactional
    public boolean updateDiscussion(Long id, DiscussionRequestVM requestVM, Integer operatorId) {
        if (!validateDiscussionOwner(id, operatorId)) {
            return false;
        }

        Discussion discussion = new Discussion();
        discussion.setId(id);
        discussion.setTitle(requestVM.getTitle());
        discussion.setContent(requestVM.getContent());
        discussion.setUpdateTime(new Date());

        return discussionMapper.updateByPrimaryKeySelective(discussion) > 0;
    }

    @Override
    @Transactional
    public boolean updateTeacherDiscussion(Long id,
            com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionRequestVM requestVM, Integer operatorId) {
        if (!validateDiscussionOwner(id, operatorId)) {
            return false;
        }

        Discussion discussion = new Discussion();
        discussion.setId(id);
        discussion.setTitle(requestVM.getTitle());
        discussion.setContent(requestVM.getContent());
        discussion.setIsTop(requestVM.getIsTop());
        discussion.setIsEssence(requestVM.getIsEssence());
        discussion.setUpdateTime(new Date());

        return discussionMapper.updateByPrimaryKeySelective(discussion) > 0;
    }

    @Override
    @Transactional
    public boolean deleteDiscussion(Long id, Integer operatorId) {
        if (!validateDiscussionOwner(id, operatorId)) {
            return false;
        }

        return discussionMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public void increaseViewCount(Long id) {
        discussionMapper.increaseViewCount(id);
    }

    @Override
    public void increaseReplyCount(Long id) {
        discussionMapper.increaseReplyCount(id);
    }

    @Override
    public void decreaseReplyCount(Long id) {
        discussionMapper.decreaseReplyCount(id);
    }

    @Override
    @Transactional
    public boolean toggleTopDiscussion(Long id, Integer teacherId) {
        Discussion discussion = discussionMapper.selectByPrimaryKey(id);
        if (discussion == null) {
            return false;
        }

        // 验证教师权限
        if (!teacherCourseService.validateTeacherCourse(teacherId, discussion.getCourseId())) {
            return false;
        }

        Discussion updateDiscussion = new Discussion();
        updateDiscussion.setId(id);
        updateDiscussion.setIsTop(!discussion.getIsTop());
        updateDiscussion.setUpdateTime(new Date());

        return discussionMapper.updateByPrimaryKeySelective(updateDiscussion) > 0;
    }

    @Override
    @Transactional
    public boolean toggleEssenceDiscussion(Long id, Integer teacherId) {
        Discussion discussion = discussionMapper.selectByPrimaryKey(id);
        if (discussion == null) {
            return false;
        }

        // 验证教师权限
        if (!teacherCourseService.validateTeacherCourse(teacherId, discussion.getCourseId())) {
            return false;
        }

        Discussion updateDiscussion = new Discussion();
        updateDiscussion.setId(id);
        updateDiscussion.setIsEssence(!discussion.getIsEssence());
        updateDiscussion.setUpdateTime(new Date());

        return discussionMapper.updateByPrimaryKeySelective(updateDiscussion) > 0;
    }

    @Override
    public List<DiscussionResponseVM> getTopDiscussions(Long courseId) {
        List<Discussion> discussions = discussionMapper.selectTopDiscussions(courseId);
        List<DiscussionResponseVM> responseVMs = new ArrayList<>();
        for (Discussion discussion : discussions) {
            responseVMs.add(convertToResponseVM(discussion));
        }
        return responseVMs;
    }

    @Override
    public List<DiscussionResponseVM> getEssenceDiscussions(Long courseId) {
        List<Discussion> discussions = discussionMapper.selectEssenceDiscussions(courseId);
        List<DiscussionResponseVM> responseVMs = new ArrayList<>();
        for (Discussion discussion : discussions) {
            responseVMs.add(convertToResponseVM(discussion));
        }
        return responseVMs;
    }

    @Override
    public boolean validateCourseAccess(Long courseId, Integer userId, Integer userType) {
        if (userType == 1) { // 学生
            CourseStudent courseStudent = courseStudentMapper.selectByCourseIdAndStudentId(courseId, userId);
            return courseStudent != null && courseStudent.getStatus() == 1; // 1表示已选课状态
        } else if (userType == 2) { // 教师
            return teacherCourseService.validateTeacherCourse(userId, courseId);
        }
        return false;
    }

    @Override
    public boolean validateDiscussionOwner(Long discussionId, Integer userId) {
        return discussionMapper.validateDiscussionOwner(discussionId, userId) > 0;
    }

    private DiscussionResponseVM convertToResponseVM(Discussion discussion) {
        DiscussionResponseVM responseVM = new DiscussionResponseVM();
        responseVM.setId(discussion.getId());
        responseVM.setTitle(discussion.getTitle());
        responseVM.setContent(discussion.getContent());
        responseVM.setCourseId(discussion.getCourseId());
        responseVM.setCreatorId(discussion.getCreatorId());
        responseVM.setCreatorType(discussion.getCreatorType());
        responseVM.setViewCount(discussion.getViewCount());
        responseVM.setReplyCount(discussion.getReplyCount());
        responseVM.setIsTop(discussion.getIsTop());
        responseVM.setIsEssence(discussion.getIsEssence());
        responseVM.setCreateTime(discussion.getCreateTime());
        responseVM.setUpdateTime(discussion.getUpdateTime());

        // 设置创建者姓名
        User creator = userMapper.selectByPrimaryKey(discussion.getCreatorId());
        if (creator != null) {
            responseVM.setCreatorName(creator.getRealName());
        }

        return responseVM;
    }
}