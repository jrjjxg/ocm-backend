package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DiscussionReply implements Serializable {
    private static final long serialVersionUID = -8745334476842374125L;

    /**
     * 回复唯一标识
     */
    private Long id;

    /**
     * 关联讨论主题表ID
     */
    private Long discussionId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复者用户ID
     */
    private Integer replierId;

    /**
     * 回复者类型(1:学生,2:教师)
     */
    private Integer replierType;

    /**
     * 回复的父评论ID，用于二级回复
     */
    private Long parentId;

    /**
     * 回复创建时间
     */
    private Date createTime;

    /**
     * 回复信息更新时间
     */
    private Date updateTime;

    // 非数据库字段，用于前端展示
    private String replierName;
    private String parentReplierName;
    private List<DiscussionReply> childReplies;

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

    public String getReplierName() {
        return replierName;
    }

    public void setReplierName(String replierName) {
        this.replierName = replierName;
    }

    public String getParentReplierName() {
        return parentReplierName;
    }

    public void setParentReplierName(String parentReplierName) {
        this.parentReplierName = parentReplierName;
    }

    public List<DiscussionReply> getChildReplies() {
        return childReplies;
    }

    public void setChildReplies(List<DiscussionReply> childReplies) {
        this.childReplies = childReplies;
    }
}