package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

public class CourseTeacher implements Serializable {
    private static final long serialVersionUID = -1410888235255083967L;

    private Long id;

    /**
     * 关联课程表ID (t_course.id)
     */
    private Long courseId;

    /**
     * 关联教师用户ID (t_user.id)
     */
    private Integer teacherId;

    /**
     * 教师对课程的权限类型
     */
    private Integer authType;

    /**
     * 关联记录创建者（管理员）ID (t_user.id)
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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
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