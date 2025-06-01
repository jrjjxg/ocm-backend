package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

public class CourseStudent implements Serializable {
    private static final long serialVersionUID = -8581687714126913503L;

    private Long id;

    /**
     * 关联课程表ID (t_course.id)
     */
    private Long courseId;

    /**
     * 关联学生用户ID (t_user.id)
     */
    private Integer studentId;

    /**
     * 选课状态(1:已选,2:已退)
     */
    private Integer status;

    /**
     * 学生课程最终成绩
     */
    private Float finalScore;

    /**
     * 关联记录创建者ID (t_user.id)
     */
    private Integer creatorId;

    /**
     * 关联记录创建时间
     */
    private Date createTime;

    /**
     * 关联记录更新时间
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

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Float finalScore) {
        this.finalScore = finalScore;
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