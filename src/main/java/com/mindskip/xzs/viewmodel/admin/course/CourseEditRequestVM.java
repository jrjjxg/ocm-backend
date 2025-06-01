package com.mindskip.xzs.viewmodel.admin.course;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CourseEditRequestVM {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private String description;

    @NotBlank
    private String semester;

    @NotNull
    private Float credit;

    @NotNull
    private Integer status;

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
}