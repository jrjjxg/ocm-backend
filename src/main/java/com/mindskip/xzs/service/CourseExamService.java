package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.CourseExam;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.viewmodel.teacher.CourseExamRequestVM;
import com.mindskip.xzs.viewmodel.teacher.CourseExamResponseVM;
import com.mindskip.xzs.viewmodel.student.StudentExamResponseVM;

import java.util.List;

/**
 * 课程测验服务接口
 */
public interface CourseExamService extends BaseService<CourseExam> {
    /**
     * 根据课程ID获取所有测验
     *
     * @param courseId 课程ID
     * @return 测验列表
     */
    List<CourseExam> findByCourseId(Long courseId);

    /**
     * 根据课程ID和测验ID获取测验
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 测验信息
     */
    CourseExam findByCourseIdAndExamId(Long courseId, Integer examId);

    /**
     * 创建课程测验
     *
     * @param courseExam 测验信息
     * @param user       创建者
     * @return 创建的测验
     */
    CourseExam create(CourseExam courseExam, User user);

    /**
     * 更新课程测验
     *
     * @param courseExam 测验信息
     * @return 更新后的测验
     */
    CourseExam update(CourseExam courseExam);

    /**
     * 删除课程测验
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     */
    void delete(Long courseId, Integer examId);

    /**
     * 根据ID查询课程测验
     *
     * @param id 测验ID (Long类型)
     * @return 测验信息
     */
    CourseExam selectById(Long id);

    /**
     * 根据ID删除课程测验
     *
     * @param id 测验ID (Long类型)
     * @return 影响行数
     */
    int deleteById(Long id);

    // ========== 新增的控制器需要的方法 ==========

    /**
     * 创建课程测验
     */
    CourseExam createCourseExam(Long courseId, CourseExamRequestVM requestVM, User currentUser);

    /**
     * 更新课程测验
     */
    CourseExam updateCourseExam(Long courseId, Long examId, CourseExamRequestVM requestVM, User currentUser);

    /**
     * 删除课程测验
     */
    void deleteCourseExam(Long courseId, Long examId, User currentUser);

    /**
     * 获取课程测验列表（教师端）
     */
    List<CourseExamResponseVM> getCourseExamsForTeacher(Long courseId);

    /**
     * 获取课程测验详情（教师端）
     */
    CourseExamResponseVM getCourseExamDetailForTeacher(Long courseId, Long examId);

    /**
     * 获取课程测验列表（学生端）
     */
    List<StudentExamResponseVM> getCourseExamsForStudent(Long courseId, Integer studentId);

    /**
     * 获取课程测验详情（学生端）
     */
    StudentExamResponseVM getCourseExamDetailForStudent(Long courseId, Long examId, Integer studentId);

    /**
     * 检查学生是否可以参加测验
     */
    boolean canStudentTakeExam(Long courseId, Long examId, Integer studentId);

    /**
     * 开始测验
     */
    Integer startExam(Long courseId, Long examId, Integer studentId);

    /**
     * 提交测验
     */
    void submitExam(Long courseId, Long examId, Integer studentId, Integer examPaperAnswerId);

    /**
     * 更新测验状态
     */
    void updateExamStatus(Long examId, Integer status);

    /**
     * 获取测验统计信息
     */
    CourseExamResponseVM getExamStatistics(Long courseId, Long examId);

    /**
     * 获取学生测验结果
     */
    Object getExamResultForStudent(Long courseId, Long examId, Integer studentId);

    /**
     * 保存学生测验草稿
     */
    void saveExamDraft(Long courseId, Long examId, Integer studentId, Object draftData);

    /**
     * 获取学生答题记录
     */
    Object getStudentExamAnswers(Long courseId, Long examId, Integer studentId);
}