package com.mindskip.xzs.viewmodel.teacher.exam;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 测验成绩响应ViewModel
 */
public class ExamResultResponseVM {
    private Integer id;
    private Integer paperId;
    private String paperName;
    private String studentId;
    private String studentName;
    private String studentUserName;
    private Integer systemScore;
    private Integer userScore;
    private Integer paperScore;
    private Integer questionCorrect;
    private Integer questionCount;
    private Integer doTime; // 答题时间(秒)
    private Integer status; // 答卷状态(1未批改 2已批改)
    private Integer score; // 成绩
    private Boolean answerCompleted; // 是否已完成答题
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime; // 提交时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public Integer getSystemScore() {
        return systemScore;
    }

    public void setSystemScore(Integer systemScore) {
        this.systemScore = systemScore;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Integer getPaperScore() {
        return paperScore;
    }

    public void setPaperScore(Integer paperScore) {
        this.paperScore = paperScore;
    }

    public Integer getQuestionCorrect() {
        return questionCorrect;
    }

    public void setQuestionCorrect(Integer questionCorrect) {
        this.questionCorrect = questionCorrect;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getDoTime() {
        return doTime;
    }

    public void setDoTime(Integer doTime) {
        this.doTime = doTime;
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

    public Boolean getAnswerCompleted() {
        return answerCompleted;
    }

    public void setAnswerCompleted(Boolean answerCompleted) {
        this.answerCompleted = answerCompleted;
    }
} 