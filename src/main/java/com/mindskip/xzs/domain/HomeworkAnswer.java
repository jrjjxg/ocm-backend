package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生作业答题记录实体类
 */
public class HomeworkAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 答题记录唯一标识
     */
    private Integer id;

    /**
     * 作业ID
     */
    private Integer homeworkId;

    /**
     * 题目ID
     */
    private Integer questionId;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 学生答案内容
     */
    private String answer;

    /**
     * 答案详细内容ID(用于存储富文本、图片等)
     */
    private Integer answerTextContentId;

    /**
     * 上传文件URLs(JSON格式)
     */
    private Integer fileUrlsTextContentId;

    /**
     * 得分(千分制)
     */
    private Integer score;

    /**
     * 自动评分
     */
    private Integer autoScore;

    /**
     * 人工评分
     */
    private Integer manualScore;

    /**
     * 是否正确
     */
    private Boolean isCorrect;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 评分时间
     */
    private Date gradedTime;

    /**
     * 评分教师ID
     */
    private Integer graderId;

    /**
     * 评分反馈
     */
    private Integer feedbackTextContentId;

    /**
     * 答题状态 1.未答 2.已答 3.已评分
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Integer getAnswerTextContentId() {
        return answerTextContentId;
    }

    public void setAnswerTextContentId(Integer answerTextContentId) {
        this.answerTextContentId = answerTextContentId;
    }

    public Integer getFileUrlsTextContentId() {
        return fileUrlsTextContentId;
    }

    public void setFileUrlsTextContentId(Integer fileUrlsTextContentId) {
        this.fileUrlsTextContentId = fileUrlsTextContentId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getAutoScore() {
        return autoScore;
    }

    public void setAutoScore(Integer autoScore) {
        this.autoScore = autoScore;
    }

    public Integer getManualScore() {
        return manualScore;
    }

    public void setManualScore(Integer manualScore) {
        this.manualScore = manualScore;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getGradedTime() {
        return gradedTime;
    }

    public void setGradedTime(Date gradedTime) {
        this.gradedTime = gradedTime;
    }

    public Integer getGraderId() {
        return graderId;
    }

    public void setGraderId(Integer graderId) {
        this.graderId = graderId;
    }

    public Integer getFeedbackTextContentId() {
        return feedbackTextContentId;
    }

    public void setFeedbackTextContentId(Integer feedbackTextContentId) {
        this.feedbackTextContentId = feedbackTextContentId;
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