package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

public class Discussion implements Serializable {
    private static final long serialVersionUID = -8726344476842374125L;

    /**
     * 讨论主题唯一标识
     */
    private Long id;

    /**
     * 讨论主题标题
     */
    private String title;

    /**
     * 讨论主题内容
     */
    private String content;

    /**
     * 关联课程表ID
     */
    private Long courseId;

    /**
     * 主题创建者ID
     */
    private Integer creatorId;

    /**
     * 创建者类型(1:学生,2:教师)
     */
    private Integer creatorType;

    /**
     * 主题被浏览次数
     */
    private Integer viewCount;

    /**
     * 主题被回复次数
     */
    private Integer replyCount;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否精华
     */
    private Boolean isEssence;

    /**
     * 主题创建时间
     */
    private Date createTime;

    /**
     * 主题信息更新时间
     */
    private Date updateTime;

    // 非数据库字段，用于前端展示
    private String creatorName;
    private String courseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(Integer creatorType) {
        this.creatorType = creatorType;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    public Boolean getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(Boolean isEssence) {
        this.isEssence = isEssence;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}