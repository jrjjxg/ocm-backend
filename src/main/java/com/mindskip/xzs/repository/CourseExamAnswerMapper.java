package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.CourseExamAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程测验答卷Mapper接口
 */
@Mapper
public interface CourseExamAnswerMapper {
    
    /**
     * 根据ID查询课程测验答卷
     */
    CourseExamAnswer selectByPrimaryKey(Long id);
    
    /**
     * 插入课程测验答卷
     */
    int insert(CourseExamAnswer record);
    
    /**
     * 选择性插入课程测验答卷
     */
    int insertSelective(CourseExamAnswer record);
    
    /**
     * 根据ID更新课程测验答卷
     */
    int updateByPrimaryKey(CourseExamAnswer record);
    
    /**
     * 选择性更新课程测验答卷
     */
    int updateByPrimaryKeySelective(CourseExamAnswer record);
    
    /**
     * 根据ID删除课程测验答卷
     */
    int deleteByPrimaryKey(Long id);
    
    /**
     * 根据课程测验ID和学生ID查询答卷记录
     */
    List<CourseExamAnswer> selectByCourseExamIdAndStudentId(@Param("courseExamId") Long courseExamId, @Param("studentId") Integer studentId);
    
    /**
     * 根据课程测验ID查询所有答卷记录
     */
    List<CourseExamAnswer> selectByCourseExamId(@Param("courseExamId") Long courseExamId);
    
    /**
     * 查询学生在某个测验中的答题次数
     */
    Integer countByStudentAndCourseExam(@Param("courseExamId") Long courseExamId, @Param("studentId") Integer studentId);
    
    /**
     * 查询学生在某个测验中的最新答卷记录
     */
    CourseExamAnswer selectLatestByStudentAndCourseExam(@Param("courseExamId") Long courseExamId, @Param("studentId") Integer studentId);
    
    /**
     * 统计课程测验的参与人数
     */
    Integer countParticipantsByCourseExamId(@Param("courseExamId") Long courseExamId);
    
    /**
     * 统计课程测验的提交人数
     */
    Integer countSubmissionsByCourseExamId(@Param("courseExamId") Long courseExamId);
}
