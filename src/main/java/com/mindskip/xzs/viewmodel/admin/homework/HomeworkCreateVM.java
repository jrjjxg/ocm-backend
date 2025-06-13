package com.mindskip.xzs.viewmodel.admin.homework;

import com.mindskip.xzs.viewmodel.BaseVM;
import java.util.Date;
import java.util.List;

/**
 * 作业创建视图模型
 */
public class HomeworkCreateVM extends BaseVM {

    private Integer id;
    private Integer courseId;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer totalScore;
    private Integer status;
    private Integer teacherId;
    private List<HomeworkQuestionVM> questions;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
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

    public List<HomeworkQuestionVM> getQuestions() {
        return questions;
    }

    public void setQuestions(List<HomeworkQuestionVM> questions) {
        this.questions = questions;
    }

    /**
     * 作业题目视图模型
     */
    public static class HomeworkQuestionVM {
        private Integer questionId;
        private Integer score;
        private Integer questionType;
        private String content;

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

        public Integer getQuestionType() {
            return questionType;
        }

        public void setQuestionType(Integer questionType) {
            this.questionType = questionType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}