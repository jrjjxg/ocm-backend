package com.mindskip.xzs.viewmodel.admin.course;

import javax.validation.constraints.NotNull;

public class CourseStudentRequestVM {
    @NotNull
    private Long courseId;

    @NotNull
    private Integer studentId;
    
    private Float finalScore;

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
    
    public Float getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Float finalScore) {
        this.finalScore = finalScore;
    }
} 