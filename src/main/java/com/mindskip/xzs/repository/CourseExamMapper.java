package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.CourseExam;
import com.mindskip.xzs.viewmodel.teacher.CourseExamResponseVM;
import com.mindskip.xzs.viewmodel.student.StudentExamResponseVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程测验数据访问接口
 */
@Mapper
public interface CourseExamMapper extends BaseMapper<CourseExam> {
    /**
     * 根据课程ID获取所有测验
     *
     * @param courseId 课程ID
     * @return 测验列表
     */
    List<CourseExam> findByCourseId(@Param("courseId") Long courseId);

    /**
     * 根据课程ID和测验ID获取测验
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 测验信息
     */
    CourseExam findByCourseIdAndExamId(@Param("courseId") Long courseId, @Param("examId") Integer examId);

    /**
     * 根据ID获取测验
     *
     * @param id 测验ID
     * @return 测验信息
     */
    CourseExam selectByPrimaryKeyLong(Long id);

    /**
     * 删除测验
     *
     * @param id 测验ID
     * @return 影响行数
     */
    int deleteByPrimaryKeyLong(Long id);

    /**
     * 删除课程测验
     *
     * @param courseId 课程ID
     * @param examId   试卷ID
     * @return 影响行数
     */
    int deleteByCourseIdAndExamId(@Param("courseId") Long courseId, @Param("examId") Integer examId);

    /**
     * 实现BaseMapper接口的方法
     */
    @Override
    int deleteByPrimaryKey(Integer id);

    @Override
    CourseExam selectByPrimaryKey(Integer id);

    /**
     * 插入测验
     *
     * @param record 测验信息
     * @return 影响行数
     */
    int insert(CourseExam record);

    /**
     * 选择性插入测验
     *
     * @param record 测验信息
     * @return 影响行数
     */
    int insertSelective(CourseExam record);

    /**
     * 选择性更新测验
     *
     * @param record 测验信息
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(CourseExam record);

    /**
     * 更新测验
     *
     * @param record 测验信息
     * @return 影响行数
     */
    int updateByPrimaryKey(CourseExam record);

    // ========== 新增的方法 ==========

    /**
     * 获取课程测验列表（教师端）
     */
    List<CourseExamResponseVM> selectByCourseIdForTeacher(@Param("courseId") Long courseId);

    /**
     * 获取课程测验详情（教师端）
     */
    CourseExamResponseVM selectByCourseIdAndExamIdForTeacher(@Param("courseId") Long courseId, @Param("examId") Long examId);

    /**
     * 获取课程测验列表（学生端）
     */
    List<StudentExamResponseVM> selectByCourseIdForStudent(@Param("courseId") Long courseId, @Param("studentId") Integer studentId);

    /**
     * 获取课程测验详情（学生端）
     */
    StudentExamResponseVM selectByCourseIdAndExamIdForStudent(@Param("courseId") Long courseId, @Param("examId") Long examId, @Param("studentId") Integer studentId);

    /**
     * 计算测验平均分
     */
    Double calculateAverageScoreByCourseExamId(@Param("courseExamId") Long courseExamId);
}