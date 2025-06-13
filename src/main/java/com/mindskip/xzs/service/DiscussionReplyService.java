package com.mindskip.xzs.service;

import com.mindskip.xzs.viewmodel.student.discussion.DiscussionReplyRequestVM;
import com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionReplyResponseVM;

import java.util.List;

public interface DiscussionReplyService {

    /**
     * 创建讨论回复
     */
    Long createReply(DiscussionReplyRequestVM requestVM, Integer replierId, Integer replierType);

    /**
     * 创建教师讨论回复
     */
    Long createTeacherReply(com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionReplyRequestVM requestVM,
            Integer replierId);

    /**
     * 根据讨论ID分页获取回复列表
     */
    List<DiscussionReplyResponseVM> getRepliesByDiscussionId(Long discussionId, Integer pageIndex, Integer pageSize);

    /**
     * 获取回复总数
     */
    int getReplyCountByDiscussionId(Long discussionId);

    /**
     * 根据ID获取回复详情
     */
    DiscussionReplyResponseVM getReplyById(Long id);

    /**
     * 删除回复
     */
    boolean deleteReply(Long id, Integer operatorId);

    /**
     * 验证回复所有者权限
     */
    boolean validateReplyOwner(Long replyId, Integer userId);

    /**
     * 根据父回复ID获取子回复
     */
    List<DiscussionReplyResponseVM> getChildReplies(Long parentId);
}