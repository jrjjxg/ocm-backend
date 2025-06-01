package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业提交实体类
 */
public class AssignmentSubmit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提交记录唯一标识
     */
    private Long id;

    /**
     * 关联作业表ID
     */
    private Long assignmentId;

    /**
     * 关联学生用户ID
     */
    private Integer studentId;

    /**
     * 学生提交作业的文字说明
     */
    private String content;

    /**
     * 学生提交的作业文件URL
     */
    private String attachmentUrl;

    /**
     * 学生提交作业的时间
     */
    private Date submitTime;

    /**
     * 批改状态(1:未批改,2:已批改)
     */
    private Integer status;

    /**
     * 作业得分
     */
    private BigDecimal score;

    /**
     * 教师批改评语
     */
    private String comment;

    /**
     * 教师批改时间
     */
    private Date checkTime;

    /**
     * 批改教师ID
     */
    private Integer teacherId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
} 