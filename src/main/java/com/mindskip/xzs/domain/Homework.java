package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业实体类
 */
public class Homework implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作业唯一标识
     */
    private Integer id;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 关联课程ID
     */
    private Integer courseId;

    /**
     * 创建者（教师）ID
     */
    private Integer createUser;

    /**
     * 作业开始时间
     */
    private Date startTime;

    /**
     * 作业结束时间
     */
    private Date endTime;

    /**
     * 作业总分(千分制)
     */
    private Integer totalScore;

    /**
     * 答题次数限制
     */
    private Integer attemptLimit;

    /**
     * 时间限制(分钟)
     */
    private Integer timeLimit;

    /**
     * 作业状态 1.草稿 2.发布 3.结束
     */
    private Integer status;

    /**
     * 作业设置(JSON格式)
     * 包含：题目乱序、选项乱序、防作弊等设置
     */
    private Integer settingsTextContentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getAttemptLimit() {
        return attemptLimit;
    }

    public void setAttemptLimit(Integer attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSettingsTextContentId() {
        return settingsTextContentId;
    }

    public void setSettingsTextContentId(Integer settingsTextContentId) {
        this.settingsTextContentId = settingsTextContentId;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}