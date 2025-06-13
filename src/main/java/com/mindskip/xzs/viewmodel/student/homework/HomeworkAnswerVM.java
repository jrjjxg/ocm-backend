package com.mindskip.xzs.viewmodel.student.homework;

import com.mindskip.xzs.viewmodel.BaseVM;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * 学生作业答题视图模型
 */
public class HomeworkAnswerVM extends BaseVM {

    private Integer homeworkId;
    private Integer questionId;
    private String answer;
    private List<String> fileUrls;
    private Integer studentId;
    private Integer userScore;
    private Integer status;
    private Date submitTime;

    /**
     * 所有题目的答案数据 (questionId -> answer)
     */
    private Map<String, Object> answers;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Map<String, Object> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Object> answers) {
        this.answers = answers;
    }
}