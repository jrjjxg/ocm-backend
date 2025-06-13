package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mindskip.xzs.viewmodel.student.dashboard.PaperFilter;
import com.mindskip.xzs.viewmodel.student.dashboard.PaperInfo;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperPageVM;
import com.mindskip.xzs.viewmodel.teacher.exam.CourseExamRequestVM;
import com.mindskip.xzs.viewmodel.teacher.exam.CourseExamResponseVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExamPaperService extends BaseService<ExamPaper> {

    PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM);

    ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, User user);

    ExamPaperEditRequestVM examPaperToVM(Integer id);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();

    List<Integer> selectMothCount();

    /**
     * 获取课程的所有测验
     *
     * @param courseId 课程ID
     * @return 测验列表
     */
    List<CourseExamResponseVM> getCourseExams(Long courseId);

    /**
     * 获取测验详情
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 测验详情
     */
    CourseExamResponseVM getCourseExam(Long courseId, Integer examId);

    /**
     * 保存课程测验
     *
     * @param model 测验信息
     * @param user  创建者
     * @return 创建的测验
     */
    ExamPaper saveCourseExam(CourseExamRequestVM model, User user);

    /**
     * 更新课程测验
     *
     * @param model 测验信息
     * @param user  更新者
     * @return 更新后的测验
     */
    ExamPaper updateCourseExam(CourseExamRequestVM model, User user);

    /**
     * 删除课程测验
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 删除结果
     */
    void deleteCourseExam(Long courseId, Integer examId);

    /**
     * 获取教师可用的试卷列表
     *
     * @param teacherId 教师ID
     * @return 试卷列表
     */
    List<ExamPaper> getAvailablePapers(Integer teacherId);

    /**
     * 教师端试卷分页查询
     *
     * @param requestVM 查询条件
     * @return 分页结果
     */
    PageInfo<ExamPaper> teacherPage(ExamPaperPageRequestVM requestVM);
}
