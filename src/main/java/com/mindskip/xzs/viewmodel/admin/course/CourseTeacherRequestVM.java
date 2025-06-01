package com.mindskip.xzs.viewmodel.admin.course;

import javax.validation.constraints.NotNull;

public class CourseTeacherRequestVM {
    @NotNull
    private Long courseId;

    @NotNull
    private Integer teacherId;

    private Integer authType;

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
} 