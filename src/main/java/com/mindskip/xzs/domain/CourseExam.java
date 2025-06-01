package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程测验关联实体类
 */
public class CourseExam implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 关联记录唯一标识
     */
    private Long id;

    /**
     * 关联课程表ID (t_course.id)
     */
    private Long courseId;

    /**
     * 关联试卷表ID (t_exam_paper.id)
     */
    private Integer examId;

    /**
     * 测验标题
     */
    private String title;

    /**
     * 测验说明
     */
    private String description;

    /**
     * 测验开始时间
     */
    private Date startTime;

    /**
     * 测验结束时间
     */
    private Date endTime;

    /**
     * 测验时长(分钟)
     */
    private Integer duration;

    /**
     * 是否打乱题目顺序
     */
    private Boolean shuffleQuestions;

    /**
     * 是否显示测验结果
     */
    private Boolean showResult;

    /**
     * 是否限制IP
     */
    private Boolean limitIp;

    /**
     * 创建者ID (t_user.id)
     */
    private Integer creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getShuffleQuestions() {
        return shuffleQuestions;
    }

    public void setShuffleQuestions(Boolean shuffleQuestions) {
        this.shuffleQuestions = shuffleQuestions;
    }

    public Boolean getShowResult() {
        return showResult;
    }

    public void setShowResult(Boolean showResult) {
        this.showResult = showResult;
    }

    public Boolean getLimitIp() {
        return limitIp;
    }

    public void setLimitIp(Boolean limitIp) {
        this.limitIp = limitIp;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
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