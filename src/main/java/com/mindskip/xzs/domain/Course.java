package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable {
    private static final long serialVersionUID = -7144143076120327288L;

    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程代码
     */
    private String code;

    /**
     * 课程详细描述
     */
    private String description;

    /**
     * 开课学期
     */
    private String semester;

    /**
     * 课程学分
     */
    private Float credit;

    /**
     * 课程状态(1:启用,0:禁用)
     */
    private Integer status;

    /**
     * 课程创建者（管理员）ID
     */
    private Integer creatorId;

    /**
     * 课程创建时间
     */
    private Date createTime;

    /**
     * 课程信息更新时间
     */
    private Date updateTime;

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