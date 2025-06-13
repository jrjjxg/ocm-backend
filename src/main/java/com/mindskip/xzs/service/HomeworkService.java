package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.Homework;
import com.mindskip.xzs.domain.HomeworkAnswer;
import com.mindskip.xzs.domain.HomeworkQuestion;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkCreateVM;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkPageRequestVM;
import com.mindskip.xzs.viewmodel.student.homework.HomeworkAnswerVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 作业服务接口
 */
public interface HomeworkService extends BaseService<Homework> {

    /**
     * 分页查询作业列表
     */
    PageInfo<Homework> page(HomeworkPageRequestVM requestVM);

    /**
     * 创建作业
     */
    void createHomework(HomeworkCreateVM createVM);

    /**
     * 获取作业详情（包含题目）
     */
    Homework getHomeworkWithQuestions(Integer homeworkId);

    /**
     * 获取作业的题目列表
     */
    List<HomeworkQuestion> getHomeworkQuestions(Integer homeworkId);

    /**
     * 学生提交答案
     */
    void submitAnswer(HomeworkAnswerVM answerVM);

    /**
     * 自动评分
     */
    void autoGrade(Integer homeworkId, Integer studentId);

    /**
     * 人工评分
     */
    void manualGrade(Integer answerId, Integer score, String feedback);

    /**
     * 获取学生作业答题情况
     */
    List<HomeworkAnswer> getStudentAnswers(Integer homeworkId, Integer studentId);

    /**
     * 获取作业统计信息
     */
    Object getHomeworkStatistics(Integer homeworkId);

    /**
     * 获取仪表板统计数据
     */
    Object getDashboardStatistics(Integer teacherId);

    /**
     * 发布作业
     */
    void publishHomework(Integer homeworkId);

    /**
     * 结束作业
     */
    void endHomework(Integer homeworkId);

    /**
     * 更新作业
     */
    void updateHomework(HomeworkCreateVM updateVM);

    /**
     * 根据课程ID获取作业列表
     */
    List<Homework> getByCourseId(Integer courseId);

    /**
     * 获取学生的作业答案
     */
    HomeworkAnswer getStudentAnswer(Integer homeworkId, Integer studentId);

    /**
     * 获取作业的所有答案记录
     */
    List<HomeworkAnswer> getAnswersByHomeworkId(Integer homeworkId);
}