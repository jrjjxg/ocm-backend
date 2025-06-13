package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.CourseExam;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.repository.CourseExamMapper;
import com.mindskip.xzs.service.CourseExamService;
import com.mindskip.xzs.service.ExamPaperService;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.viewmodel.teacher.CourseExamRequestVM;
import com.mindskip.xzs.viewmodel.teacher.CourseExamResponseVM;
import com.mindskip.xzs.viewmodel.student.StudentExamResponseVM;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 课程测验服务实现类
 */
@Service
public class CourseExamServiceImpl extends BaseServiceImpl<CourseExam> implements CourseExamService {

    private final CourseExamMapper courseExamMapper;

    @Autowired
    @Lazy
    private ExamPaperService examPaperService;

    @Autowired
    private CourseStudentService courseStudentService;

    @Autowired
    public CourseExamServiceImpl(CourseExamMapper courseExamMapper) {
        super(courseExamMapper);
        this.courseExamMapper = courseExamMapper;
    }

    @Override
    public List<CourseExam> findByCourseId(Long courseId) {
        return courseExamMapper.findByCourseId(courseId);
    }

    @Override
    public CourseExam findByCourseIdAndExamId(Long courseId, Integer examId) {
        return courseExamMapper.findByCourseIdAndExamId(courseId, examId);
    }

    @Override
    public CourseExam create(CourseExam courseExam, User user) {
        Date now = new Date();
        courseExam.setCreatorId(user.getId());
        courseExam.setCreateTime(now);
        courseExam.setUpdateTime(now);

        // 设置默认值
        if (courseExam.getShuffleQuestions() == null) {
            courseExam.setShuffleQuestions(false);
        }
        if (courseExam.getShowResult() == null) {
            courseExam.setShowResult(true);
        }
        if (courseExam.getLimitIp() == null) {
            courseExam.setLimitIp(false);
        }

        courseExamMapper.insertSelective(courseExam);
        return courseExam;
    }

    @Override
    public CourseExam update(CourseExam courseExam) {
        courseExam.setUpdateTime(new Date());
        courseExamMapper.updateByPrimaryKeySelective(courseExam);
        return courseExam;
    }

    @Override
    public void delete(Long courseId, Integer examId) {
        courseExamMapper.deleteByCourseIdAndExamId(courseId, examId);
    }

    @Override
    public CourseExam selectById(Long id) {
        return courseExamMapper.selectByPrimaryKeyLong(id);
    }

    @Override
    public int deleteById(Long id) {
        return courseExamMapper.deleteByPrimaryKeyLong(id);
    }

    // ========== 新增的控制器需要的方法实现 ==========

    @Override
    @Transactional
    public CourseExam createCourseExam(Long courseId, CourseExamRequestVM requestVM, User currentUser) {
        CourseExam courseExam = new CourseExam();
        BeanUtils.copyProperties(requestVM, courseExam);

        courseExam.setCourseId(courseId);
        courseExam.setExamId(requestVM.getExamPaperId());
        courseExam.setCreatorId(currentUser.getId());
        courseExam.setCreateTime(new Date());
        courseExam.setUpdateTime(new Date());

        // 根据时间设置状态
        Date now = new Date();
        if (now.before(courseExam.getStartTime())) {
            courseExam.setStatus(1); // 未开始
        } else if (now.after(courseExam.getEndTime())) {
            courseExam.setStatus(3); // 已结束
        } else {
            courseExam.setStatus(2); // 进行中
        }

        courseExamMapper.insertSelective(courseExam);
        return courseExam;
    }

    @Override
    @Transactional
    public CourseExam updateCourseExam(Long courseId, Long examId, CourseExamRequestVM requestVM, User currentUser) {
        CourseExam existingExam = courseExamMapper.selectByPrimaryKeyLong(examId);
        if (existingExam == null || !existingExam.getCourseId().equals(courseId)) {
            throw new RuntimeException("测验不存在或不属于该课程");
        }

        BeanUtils.copyProperties(requestVM, existingExam);
        existingExam.setExamId(requestVM.getExamPaperId());
        existingExam.setUpdateTime(new Date());

        // 根据时间更新状态
        Date now = new Date();
        if (now.before(existingExam.getStartTime())) {
            existingExam.setStatus(1); // 未开始
        } else if (now.after(existingExam.getEndTime())) {
            existingExam.setStatus(3); // 已结束
        } else {
            existingExam.setStatus(2); // 进行中
        }

        courseExamMapper.updateByPrimaryKeySelective(existingExam);
        return existingExam;
    }

    @Override
    @Transactional
    public void deleteCourseExam(Long courseId, Long examId, User currentUser) {
        CourseExam existingExam = courseExamMapper.selectByPrimaryKeyLong(examId);
        if (existingExam == null || !existingExam.getCourseId().equals(courseId)) {
            throw new RuntimeException("测验不存在或不属于该课程");
        }

        courseExamMapper.deleteByPrimaryKeyLong(examId);
    }

    @Override
    public List<CourseExamResponseVM> getCourseExamsForTeacher(Long courseId) {
        return courseExamMapper.selectByCourseIdForTeacher(courseId);
    }

    @Override
    public CourseExamResponseVM getCourseExamDetailForTeacher(Long courseId, Long examId) {
        return courseExamMapper.selectByCourseIdAndExamIdForTeacher(courseId, examId);
    }

    @Override
    public List<StudentExamResponseVM> getCourseExamsForStudent(Long courseId, Integer studentId) {
        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, studentId)) {
            throw new RuntimeException("学生未选修该课程");
        }

        return courseExamMapper.selectByCourseIdForStudent(courseId, studentId);
    }

    @Override
    public StudentExamResponseVM getCourseExamDetailForStudent(Long courseId, Long examId, Integer studentId) {
        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, studentId)) {
            throw new RuntimeException("学生未选修该课程");
        }

        return courseExamMapper.selectByCourseIdAndExamIdForStudent(courseId, examId, studentId);
    }

    @Override
    public boolean canStudentTakeExam(Long courseId, Long examId, Integer studentId) {
        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, studentId)) {
            return false;
        }

        CourseExam courseExam = courseExamMapper.selectByPrimaryKeyLong(examId);
        if (courseExam == null || !courseExam.getCourseId().equals(courseId)) {
            return false;
        }

        Date now = new Date();
        // 检查时间范围
        if (now.before(courseExam.getStartTime()) || now.after(courseExam.getEndTime())) {
            return false;
        }

        // 简化实现：暂时允许所有符合条件的学生参加测验
        return true;
    }

    @Override
    @Transactional
    public Integer startExam(Long courseId, Long examId, Integer studentId) {
        if (!canStudentTakeExam(courseId, examId, studentId)) {
            throw new RuntimeException("无法开始测验");
        }

        CourseExam courseExam = courseExamMapper.selectByPrimaryKeyLong(examId);
        if (courseExam == null) {
            throw new RuntimeException("测验不存在");
        }

        // 简化实现：直接返回试卷ID，让前端处理
        return courseExam.getExamId();
    }

    @Override
    @Transactional
    public void submitExam(Long courseId, Long examId, Integer studentId, Integer examPaperAnswerId) {
        // 简化实现：记录提交日志
        System.out.println("提交测验: courseId=" + courseId + ", examId=" + examId +
                ", studentId=" + studentId + ", examPaperAnswerId=" + examPaperAnswerId);
    }

    @Override
    public void updateExamStatus(Long examId, Integer status) {
        CourseExam courseExam = courseExamMapper.selectByPrimaryKeyLong(examId);
        if (courseExam != null) {
            courseExam.setStatus(status);
            courseExam.setUpdateTime(new Date());
            courseExamMapper.updateByPrimaryKeySelective(courseExam);
        }
    }

    @Override
    public CourseExamResponseVM getExamStatistics(Long courseId, Long examId) {
        CourseExamResponseVM responseVM = courseExamMapper.selectByCourseIdAndExamIdForTeacher(courseId, examId);
        if (responseVM != null) {
            // 简化实现：设置默认统计信息
            responseVM.setParticipantCount(0);
            responseVM.setSubmitCount(0);
            responseVM.setAverageScore(0.0);
        }
        return responseVM;
    }

    @Override
    public Object getExamResultForStudent(Long courseId, Long examId, Integer studentId) {
        // 简化实现：返回空结果
        return null;
    }

    @Override
    public void saveExamDraft(Long courseId, Long examId, Integer studentId, Object draftData) {
        // 简化实现：记录日志
        System.out.println("保存测验草稿: courseId=" + courseId + ", examId=" + examId + ", studentId=" + studentId);
    }

    @Override
    public Object getStudentExamAnswers(Long courseId, Long examId, Integer studentId) {
        // 简化实现：返回空结果
        return null;
    }
}