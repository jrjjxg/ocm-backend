package com.mindskip.xzs.viewmodel.teacher.discussion;

import java.util.Date;
import java.util.List;

public class DiscussionReplyResponseVM {

    private Long id;
    private Long discussionId;
    private String content;
    private Integer replierId;
    private String replierName;
    private Integer replierType;
    private Long parentId;
    private String parentReplierName;
    private Date createTime;
    private Date updateTime;
    private List<DiscussionReplyResponseVM> childReplies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Long discussionId) {
        this.discussionId = discussionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReplierId() {
        return replierId;
    }

    public void setReplierId(Integer replierId) {
        this.replierId = replierId;
    }

    public String getReplierName() {
        return replierName;
    }

    public void setReplierName(String replierName) {
        this.replierName = replierName;
    }

    public Integer getReplierType() {
        return replierType;
    }

    public void setReplierType(Integer replierType) {
        this.replierType = replierType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentReplierName() {
        return parentReplierName;
    }

    public void setParentReplierName(String parentReplierName) {
        this.parentReplierName = parentReplierName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<DiscussionReplyResponseVM> getChildReplies() {
        return childReplies;
    }

    public void setChildReplies(List<DiscussionReplyResponseVM> childReplies) {
        this.childReplies = childReplies;
    }
}