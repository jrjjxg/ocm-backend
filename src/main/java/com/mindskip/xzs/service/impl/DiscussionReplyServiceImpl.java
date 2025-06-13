package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.DiscussionReply;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.repository.DiscussionReplyMapper;
import com.mindskip.xzs.repository.UserMapper;
import com.mindskip.xzs.service.DiscussionReplyService;
import com.mindskip.xzs.service.DiscussionService;
import com.mindskip.xzs.viewmodel.student.discussion.DiscussionReplyRequestVM;
import com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionReplyResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiscussionReplyServiceImpl implements DiscussionReplyService {

    private final DiscussionReplyMapper discussionReplyMapper;
    private final UserMapper userMapper;
    private final DiscussionService discussionService;

    @Autowired
    public DiscussionReplyServiceImpl(DiscussionReplyMapper discussionReplyMapper,
            UserMapper userMapper,
            DiscussionService discussionService) {
        this.discussionReplyMapper = discussionReplyMapper;
        this.userMapper = userMapper;
        this.discussionService = discussionService;
    }

    @Override
    @Transactional
    public Long createReply(DiscussionReplyRequestVM requestVM, Integer replierId, Integer replierType) {
        DiscussionReply reply = new DiscussionReply();
        reply.setDiscussionId(requestVM.getDiscussionId());
        reply.setContent(requestVM.getContent());
        reply.setReplierId(replierId);
        reply.setReplierType(replierType);
        reply.setParentId(requestVM.getParentId());
        reply.setCreateTime(new Date());
        reply.setUpdateTime(new Date());

        discussionReplyMapper.insertSelective(reply);

        // 增加讨论的回复数
        discussionService.increaseReplyCount(requestVM.getDiscussionId());

        return reply.getId();
    }

    @Override
    @Transactional
    public Long createTeacherReply(com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionReplyRequestVM requestVM,
            Integer replierId) {
        DiscussionReply reply = new DiscussionReply();
        reply.setDiscussionId(requestVM.getDiscussionId());
        reply.setContent(requestVM.getContent());
        reply.setReplierId(replierId);
        reply.setReplierType(2); // 2表示教师
        reply.setParentId(requestVM.getParentId());
        reply.setCreateTime(new Date());
        reply.setUpdateTime(new Date());

        discussionReplyMapper.insertSelective(reply);

        // 增加讨论的回复数
        discussionService.increaseReplyCount(requestVM.getDiscussionId());

        return reply.getId();
    }

    @Override
    public List<DiscussionReplyResponseVM> getRepliesByDiscussionId(Long discussionId, Integer pageIndex,
            Integer pageSize) {
        Integer offset = (pageIndex - 1) * pageSize;
        List<DiscussionReply> replies = discussionReplyMapper.selectByDiscussionId(discussionId, offset, pageSize);

        List<DiscussionReplyResponseVM> responseVMs = new ArrayList<>();
        for (DiscussionReply reply : replies) {
            DiscussionReplyResponseVM responseVM = convertToResponseVM(reply);

            // 如果是一级回复，获取其子回复
            if (reply.getParentId() == null) {
                List<DiscussionReplyResponseVM> childReplies = getChildReplies(reply.getId());
                responseVM.setChildReplies(childReplies);
            }

            responseVMs.add(responseVM);
        }

        return responseVMs;
    }

    @Override
    public int getReplyCountByDiscussionId(Long discussionId) {
        return discussionReplyMapper.countByDiscussionId(discussionId);
    }

    @Override
    public DiscussionReplyResponseVM getReplyById(Long id) {
        DiscussionReply reply = discussionReplyMapper.selectByPrimaryKey(id);
        if (reply == null) {
            return null;
        }
        return convertToResponseVM(reply);
    }

    @Override
    @Transactional
    public boolean deleteReply(Long id, Integer operatorId) {
        if (!validateReplyOwner(id, operatorId)) {
            return false;
        }

        DiscussionReply reply = discussionReplyMapper.selectByPrimaryKey(id);
        if (reply == null) {
            return false;
        }

        // 删除回复
        int result = discussionReplyMapper.deleteByPrimaryKey(id);

        if (result > 0) {
            // 减少讨论的回复数
            discussionService.decreaseReplyCount(reply.getDiscussionId());
        }

        return result > 0;
    }

    @Override
    public boolean validateReplyOwner(Long replyId, Integer userId) {
        return discussionReplyMapper.validateReplyOwner(replyId, userId) > 0;
    }

    @Override
    public List<DiscussionReplyResponseVM> getChildReplies(Long parentId) {
        List<DiscussionReply> childReplies = discussionReplyMapper.selectByParentId(parentId);
        List<DiscussionReplyResponseVM> responseVMs = new ArrayList<>();

        for (DiscussionReply reply : childReplies) {
            responseVMs.add(convertToResponseVM(reply));
        }

        return responseVMs;
    }

    private DiscussionReplyResponseVM convertToResponseVM(DiscussionReply reply) {
        DiscussionReplyResponseVM responseVM = new DiscussionReplyResponseVM();
        responseVM.setId(reply.getId());
        responseVM.setDiscussionId(reply.getDiscussionId());
        responseVM.setContent(reply.getContent());
        responseVM.setReplierId(reply.getReplierId());
        responseVM.setReplierType(reply.getReplierType());
        responseVM.setParentId(reply.getParentId());
        responseVM.setCreateTime(reply.getCreateTime());
        responseVM.setUpdateTime(reply.getUpdateTime());

        // 设置回复者姓名
        User replier = userMapper.selectByPrimaryKey(reply.getReplierId());
        if (replier != null) {
            responseVM.setReplierName(replier.getRealName());
        }

        // 如果有父回复，设置父回复者姓名
        if (reply.getParentId() != null) {
            DiscussionReply parentReply = discussionReplyMapper.selectByPrimaryKey(reply.getParentId());
            if (parentReply != null) {
                User parentReplier = userMapper.selectByPrimaryKey(parentReply.getReplierId());
                if (parentReplier != null) {
                    responseVM.setParentReplierName(parentReplier.getRealName());
                }
            }
        }

        return responseVM;
    }
}