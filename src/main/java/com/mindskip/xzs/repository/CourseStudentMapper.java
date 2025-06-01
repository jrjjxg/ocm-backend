package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {
    List<CourseStudentDTO> getCourseStudents(@Param("courseId") Long courseId);
    
    /**
     * 获取课程学生列表（带分页和关键词搜索）
     *
     * @param courseId 课程ID
     * @param keyword 搜索关键词
     * @return 学生列表
     */
    List<CourseStudentDTO> getCourseStudentsWithPage(@Param("courseId") Long courseId, @Param("keyword") String keyword);

    CourseStudent selectByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Integer studentId);

    int updateStatus(@Param("courseId") Long courseId, @Param("studentId") Integer studentId, @Param("status") Integer status);
    
    /**
     * 查询学生已选的所有课程ID
     *
     * @param studentId 学生ID
     * @return 课程ID列表
     */
    List<Long> findCourseIdsByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 更新学生最终成绩
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @param finalScore 最终成绩
     * @return 更新的记录数
     */
    int updateFinalScore(@Param("courseId") Long courseId, @Param("studentId") Integer studentId, @Param("finalScore") Float finalScore);
    
    @Override
    int deleteByPrimaryKey(Integer id);

    int insert(CourseStudent record);

    @Override
    int insertSelective(CourseStudent record);

    @Override
    CourseStudent selectByPrimaryKey(Integer id);

    @Override
    int updateByPrimaryKeySelective(CourseStudent record);

    @Override
    int updateByPrimaryKey(CourseStudent record);
    
    // 为了保持原有功能，添加Long参数版本的方法
    int deleteByPrimaryKeyLong(Long id);
    
    CourseStudent selectByPrimaryKeyLong(Long id);
    
    /**
     * 查询课程学生列表
     *
     * @param courseId 课程ID
     * @return 学生列表
     */
    List<CourseStudentDTO> selectByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 统计课程学生总数
     *
     * @param courseId 课程ID
     * @return 学生总数
     */
    Integer countByCourseId(@Param("courseId") Long courseId);
} 