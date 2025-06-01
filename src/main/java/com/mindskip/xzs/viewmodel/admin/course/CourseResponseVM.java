package com.mindskip.xzs.viewmodel.admin.course;

import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.utility.DateTimeUtil;

public class CourseResponseVM {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String semester;
    private Float credit;
    private Integer status;
    private Integer creatorId;
    private String createTime;
    private String updateTime;

    public static CourseResponseVM from(Course course) {
        CourseResponseVM vm = new CourseResponseVM();
        vm.setId(course.getId());
        vm.setName(course.getName());
        vm.setCode(course.getCode());
        vm.setDescription(course.getDescription());
        vm.setSemester(course.getSemester());
        vm.setCredit(course.getCredit());
        vm.setStatus(course.getStatus());
        vm.setCreatorId(course.getCreatorId());
        vm.setCreateTime(DateTimeUtil.dateFormat(course.getCreateTime()));
        vm.setUpdateTime(DateTimeUtil.dateFormat(course.getUpdateTime()));
        return vm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Float getCredit() {
        return credit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}