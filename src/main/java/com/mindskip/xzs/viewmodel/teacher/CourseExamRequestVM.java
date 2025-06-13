package com.mindskip.xzs.viewmodel.teacher;

import com.mindskip.xzs.base.BasePage;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 教师端课程测验请求ViewModel
 */
public class CourseExamRequestVM extends BasePage {
    
    /**
     * 测验ID（编辑时使用）
     */
    private Long id;
    
    /**
     * 关联试卷表ID
     */
    @NotNull(message = "试卷ID不能为空")
    private Integer examPaperId;
    
    /**
     * 测验标题
     */
    @NotBlank(message = "测验标题不能为空")
    private String title;
    
    /**
     * 测验描述
     */
    private String description;
    
    /**
     * 测验开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    /**
     * 测验结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    /**
     * 测验时长(分钟)
     */
    @NotNull(message = "测验时长不能为空")
    private Integer duration;
    
    /**
     * 最大答题次数
     */
    private Integer maxAttempts = 1;
    
    /**
     * 是否打乱题目顺序
     */
    private Boolean shuffleQuestions = false;
    
    /**
     * 是否显示测验结果
     */
    private Boolean showResult = true;
    
    /**
     * 是否显示正确答案
     */
    private Boolean showAnswer = false;
    
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
}
