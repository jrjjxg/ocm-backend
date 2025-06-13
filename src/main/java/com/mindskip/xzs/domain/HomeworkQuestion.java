package com.mindskip.xzs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业题目关联实体类
 */
public class HomeworkQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联记录唯一标识
     */
    private Integer id;

    /**
     * 作业ID
     */
    private Integer homeworkId;

    /**
     * 题目ID
     */
    private Integer questionId;

    /**
     * 题目在作业中的分值(千分制)
     */
    private Integer score;

    /**
     * 题目在作业中的排序
     */
    private Integer itemOrder;

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

    public Integer getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}