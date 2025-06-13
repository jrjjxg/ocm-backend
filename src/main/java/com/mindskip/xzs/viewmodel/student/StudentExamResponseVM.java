package com.mindskip.xzs.viewmodel.student;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 学生端课程测验响应ViewModel
 */
public class StudentExamResponseVM {

    /**
     * 测验ID
     */
    private Long id;

    /**
     * 关联试卷表ID
     */
    private Integer examPaperId;

    /**
     * 测验标题
     */
    private String title;

    /**
     * 测验描述
     */
    private String description;

    /**
     * 测验开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 测验结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 测验时长(分钟)
     */
    private Integer duration;

    /**
     * 最大答题次数
     */
    private Integer maxAttempts;

    /**
     * 测验状态(1:未开始,2:进行中,3:已结束,4:已取消)
     */
    private Integer status;

    /**
     * 测验状态名称
     */
    private String statusName;

    /**
     * 提交状态
     */
    private String submitStatus;

    /**
     * 学生得分
     */
    private Integer score;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    /**
     * 学生是否可以参加测验
     */
    private Boolean canTakeExam;

    /**
     * 学生已答题次数
     */
    private Integer attemptCount;

    /**
     * 学生最高分数
     */
    private Integer bestScore;

    /**
     * 学生最近一次答题状态
     */
    private Integer lastAttemptStatus;

    /**
     * 距离开始时间（毫秒）
     */
    private Long timeToStart;

    /**
     * 距离结束时间（毫秒）
     */
    private Long timeToEnd;

    /**
     * 试卷总分
     */
    private Integer totalScore;

    /**
     * 题目数量
     */
    private Integer questionCount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Integer examPaperId) {
        this.examPaperId = examPaperId;
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

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Boolean getCanTakeExam() {
        return canTakeExam;
    }

    public void setCanTakeExam(Boolean canTakeExam) {
        this.canTakeExam = canTakeExam;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Integer getBestScore() {
        return bestScore;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    public Integer getLastAttemptStatus() {
        return lastAttemptStatus;
    }

    public void setLastAttemptStatus(Integer lastAttemptStatus) {
        this.lastAttemptStatus = lastAttemptStatus;
    }

    public Long getTimeToStart() {
        return timeToStart;
    }

    public void setTimeToStart(Long timeToStart) {
        this.timeToStart = timeToStart;
    }

    public Long getTimeToEnd() {
        return timeToEnd;
    }

    public void setTimeToEnd(Long timeToEnd) {
        this.timeToEnd = timeToEnd;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }
}
