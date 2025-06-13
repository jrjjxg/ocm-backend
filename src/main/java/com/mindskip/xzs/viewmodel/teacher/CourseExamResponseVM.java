package com.mindskip.xzs.viewmodel.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 教师端课程测验响应ViewModel
 */
public class CourseExamResponseVM {
    
    /**
     * 测验ID
     */
    private Long id;
    
    /**
     * 关联试卷表ID
     */
    private Integer examPaperId;
    
    /**
     * 试卷名称
     */
    private String examPaperName;
    
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
     * 是否打乱题目顺序
     */
    private Boolean shuffleQuestions;
    
    /**
     * 是否显示测验结果
     */
    private Boolean showResult;
    
    /**
     * 是否显示正确答案
     */
    private Boolean showAnswer;
    
    /**
     * 测验状态(1:未开始,2:进行中,3:已结束,4:已取消)
     */
    private Integer status;
    
    /**
     * 测验状态名称
     */
    private String statusName;
    
    /**
     * 参与人数
     */
    private Integer participantCount;
    
    /**
     * 已提交人数
     */
    private Integer submitCount;
    
    /**
     * 平均分
     */
    private Double averageScore;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
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
    
    public String getExamPaperName() {
        return examPaperName;
    }
    
    public void setExamPaperName(String examPaperName) {
        this.examPaperName = examPaperName;
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
    
    public Boolean getShowAnswer() {
        return showAnswer;
    }
    
    public void setShowAnswer(Boolean showAnswer) {
        this.showAnswer = showAnswer;
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
    
    public Integer getParticipantCount() {
        return participantCount;
    }
    
    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }
    
    public Integer getSubmitCount() {
        return submitCount;
    }
    
    public void setSubmitCount(Integer submitCount) {
        this.submitCount = submitCount;
    }
    
    public Double getAverageScore() {
        return averageScore;
    }
    
    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
