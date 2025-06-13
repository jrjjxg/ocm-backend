package com.mindskip.xzs.viewmodel.admin.homework;

import com.mindskip.xzs.base.BasePage;

/**
 * 作业分页请求视图模型
 */
public class HomeworkPageRequestVM extends BasePage {

    private Integer id;
    private Integer courseId;
    private String title;
    private Integer status;
    private Integer teacherId;
    private Boolean pendingGrade; // 是否只查询待批改的作业

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Boolean getPendingGrade() {
        return pendingGrade;
    }

    public void setPendingGrade(Boolean pendingGrade) {
        this.pendingGrade = pendingGrade;
    }
}