package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.CourseTeacher;
import com.mindskip.xzs.domain.dto.CourseTeacherDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseTeacherMapper {
    List<CourseTeacherDTO> getCourseTeachers(@Param("courseId") Long courseId);

    CourseTeacher selectByCourseIdAndTeacherId(@Param("courseId") Long courseId, @Param("teacherId") Integer teacherId);

    int deleteByCourseIdAndTeacherId(@Param("courseId") Long courseId, @Param("teacherId") Integer teacherId);
    
    /**
     * 查询教师关联的所有课程ID
     *
     * @param teacherId 教师ID
     * @return 课程ID列表
     */
    List<Long> findCourseIdsByTeacherId(@Param("teacherId") Integer teacherId);
    
    int deleteByPrimaryKey(Long id);

    int insert(CourseTeacher record);

    int insertSelective(CourseTeacher record);

    CourseTeacher selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CourseTeacher record);

    int updateByPrimaryKey(CourseTeacher record);
} 