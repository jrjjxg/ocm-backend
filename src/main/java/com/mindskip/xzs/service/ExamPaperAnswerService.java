package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.ExamPaperAnswer;
import com.mindskip.xzs.domain.ExamPaperAnswerInfo;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperSubmitVM;
import com.mindskip.xzs.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import com.mindskip.xzs.viewmodel.teacher.exam.ExamResultResponseVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExamPaperAnswerService extends BaseService<ExamPaperAnswer> {

    /**
     * 学生考试记录分页
     *
     * @param requestVM 过滤条件
     * @return PageInfo<ExamPaperAnswer>
     */
    PageInfo<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    /**
     * 计算试卷提交结果(不入库)
     *
     * @param examPaperSubmitVM
     * @param user
     * @return
     */
    ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user);

    /**
     * 试卷批改
     * 
     * @param examPaperSubmitVM examPaperSubmitVM
     * @return String
     */
    String judge(ExamPaperSubmitVM examPaperSubmitVM);

    /**
     * 试卷答题信息转成ViewModel 传给前台
     *
     * @param id 试卷id
     * @return ExamPaperSubmitVM
     */
    ExamPaperSubmitVM examPaperAnswerToVM(Integer id);

    Integer selectAllCount();

    List<Integer> selectMothCount();

    PageInfo<ExamPaperAnswer> adminPage(com.mindskip.xzs.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM requestVM);

    /**
     * 获取测验的学生答卷成绩列表
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 成绩列表
     */
    List<ExamResultResponseVM> getExamResults(Long courseId, Integer examId);

    /**
     * 获取测验统计数据
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 统计数据
     */
    Object getExamStatistics(Long courseId, Integer examId);

    /**
     * 获取测验答卷列表
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @param query    查询条件
     * @return 答卷列表
     */
    Object getExamAnswers(Long courseId, Integer examId, Object query);

    /**
     * 评阅测验答卷
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @param answerId 答卷ID
     * @param data     评阅数据
     */
    void evaluateAnswer(Long courseId, Integer examId, Integer answerId, Object data);

    /**
     * 创建测验答卷
     *
     * @param examPaperId 试卷ID
     * @param studentId   学生ID
     * @return 创建的答卷对象
     */
    ExamPaperAnswer createExamPaperAnswer(Integer examPaperId, Integer studentId);
}
