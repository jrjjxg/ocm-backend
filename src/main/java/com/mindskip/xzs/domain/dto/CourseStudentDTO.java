package com.mindskip.xzs.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程学生数据传输对象
 */
public class CourseStudentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Integer id;
    
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 学生用户名
     */
    private String userName;

    /**
     * 学生真实姓名
     */
    private String realName;
    
    /**
     * 学号
     */
    private String studentId;

    /**
     * 选课状态(1:已选,2:已退)
     */
    private Integer status;

    /**
     * 最终成绩
     */
    private Float finalScore;

    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
} 