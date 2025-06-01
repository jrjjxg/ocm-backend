package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业实体类
 */
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作业唯一标识
     */
    private Long id;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业描述与要求
     */
    private String description;

    /**
     * 作业附件文件URL
     */
    private String attachmentUrl;

    /**
     * 作业开始时间
     */
    private Date startTime;

    /**
     * 作业截止时间
     */
    private Date endTime;

    /**
     * 关联课程表ID
     */
    private Long courseId;

    /**
     * 作业创建者（教师）ID
     */
    private Integer creatorId;

    /**
     * 作业满分值
     */
    private Integer totalScore;

    /**
     * 作业状态(1:未开始,2:进行中,3:已结束)
     */
    private Integer status;

    /**
     * 作业创建时间
     */
    private Date createTime;

    /**
     * 作业信息更新时间
     */
    private Date updateTime;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
} 