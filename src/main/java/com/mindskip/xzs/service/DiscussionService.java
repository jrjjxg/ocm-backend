package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.Discussion;
import com.mindskip.xzs.viewmodel.student.discussion.DiscussionRequestVM;
import com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionResponseVM;

import java.util.List;

public interface DiscussionService {

    /**
     * 创建讨论主题
     */
    Long createDiscussion(DiscussionRequestVM requestVM, Integer creatorId, Integer creatorType);

    /**
     * 创建教师讨论主题
     */
    Long createTeacherDiscussion(com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionRequestVM requestVM,
            Integer creatorId);

    /**
     * 根据ID获取讨论详情
     */
    DiscussionResponseVM getDiscussionById(Long id);

    /**
     * 根据课程ID分页获取讨论列表
     */
    List<DiscussionResponseVM> getDiscussionsByCourseId(Long courseId, Integer pageIndex, Integer pageSize);

    /**
     * 获取讨论总数
     */
    int getDiscussionCountByCourseId(Long courseId);

    /**
     * 更新讨论
     */
    boolean updateDiscussion(Long id, DiscussionRequestVM requestVM, Integer operatorId);

    /**
     * 更新教师讨论
     */
    boolean updateTeacherDiscussion(Long id,
            com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionRequestVM requestVM, Integer operatorId);

    /**
     * 删除讨论
     */
    boolean deleteDiscussion(Long id, Integer operatorId);

    /**
     * 增加浏览数
     */
    void increaseViewCount(Long id);

    /**
     * 增加回复数
     */
    void increaseReplyCount(Long id);

    /**
     * 减少回复数
     */
    void decreaseReplyCount(Long id);

    /**
     * 置顶/取消置顶讨论（仅教师）
     */
    boolean toggleTopDiscussion(Long id, Integer teacherId);

    /**
     * 设置/取消精华讨论（仅教师）
     */
    boolean toggleEssenceDiscussion(Long id, Integer teacherId);

    /**
     * 获取置顶讨论
     */
    List<DiscussionResponseVM> getTopDiscussions(Long courseId);

    /**
     * 获取精华讨论
     */
    List<DiscussionResponseVM> getEssenceDiscussions(Long courseId);

    /**
     * 验证用户对课程的权限
     */
    boolean validateCourseAccess(Long courseId, Integer userId, Integer userType);

    /**
     * 验证讨论所有者权限
     */
    boolean validateDiscussionOwner(Long discussionId, Integer userId);
}