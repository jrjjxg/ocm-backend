package com.mindskip.xzs.viewmodel.teacher.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 课程测验响应ViewModel
 */
public class CourseExamResponseVM {
    private Integer id;
    private String title;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    private Integer duration;
    private Integer paperId;
    private String paperName;
    
    @JsonProperty("shuffleQuestions")
    private Boolean shuffleQuestions;
    
    @JsonProperty("showResult")
    private Boolean showResult;
    
    @JsonProperty("limitIP")
    private Boolean limitIP;
    
    private Long courseId;
    private Integer status; // 0: 未开始, 1: 进行中, 2: 已结束
    private Integer submitCount; // 已提交人数
    private Integer totalCount; // 总人数

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

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
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

    public Boolean getLimitIP() {
        return limitIP;
    }

    public void setLimitIP(Boolean limitIP) {
        this.limitIP = limitIP;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(Integer submitCount) {
        this.submitCount = submitCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
} 