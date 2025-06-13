package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程测验答卷实体类
 */
public class CourseExamAnswer implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 课程测验答卷记录唯一标识
     */
    private Long id;
    
    /**
     * 关联课程测验表ID
     */
    private Long courseExamId;
    
    /**
     * 关联答卷表ID
     */
    private Integer examPaperAnswerId;
    
    /**
     * 关联学生用户ID
     */
    private Integer studentId;
    
    /**
     * 答题次数
     */
    private Integer attemptNumber;
    
    /**
     * 开始答题时间
     */
    private Date startTime;
    
    /**
     * 提交答题时间
     */
    private Date submitTime;
    
    /**
     * 答题状态(1:进行中,2:已提交,3:已评分)
     */
    private Integer status;
    
    /**
     * 记录创建时间
     */
    private Date createTime;
    
    /**
     * 记录更新时间
     */
    private Date updateTime;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCourseExamId() {
        return courseExamId;
    }
    
    public void setCourseExamId(Long courseExamId) {
        this.courseExamId = courseExamId;
    }
    
    public Integer getExamPaperAnswerId() {
        return examPaperAnswerId;
    }
    
    public void setExamPaperAnswerId(Integer examPaperAnswerId) {
        this.examPaperAnswerId = examPaperAnswerId;
    }
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    
    public Integer getAttemptNumber() {
        return attemptNumber;
    }
    
    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
    
    @Override
    public String toString() {
        return "CourseExamAnswer{" +
                "id=" + id +
                ", courseExamId=" + courseExamId +
                ", examPaperAnswerId=" + examPaperAnswerId +
                ", studentId=" + studentId +
                ", attemptNumber=" + attemptNumber +
                ", startTime=" + startTime +
                ", submitTime=" + submitTime +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
