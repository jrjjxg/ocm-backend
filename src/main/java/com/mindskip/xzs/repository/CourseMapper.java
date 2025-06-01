package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> page(@Param("name") String name);

    Course selectByCode(@Param("code") String code);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int deleteByPrimaryKey(Long id);

    int insert(Course record);

    int insertSelective(Course record);

    Course selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKey(Course record);
} 