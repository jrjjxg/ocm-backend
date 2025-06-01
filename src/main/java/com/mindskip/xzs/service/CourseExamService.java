package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.CourseExam;
import com.mindskip.xzs.domain.User;

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
     * @param examId 测验ID
     * @return 测验信息
     */
    CourseExam findByCourseIdAndExamId(Long courseId, Integer examId);
    
    /**
     * 创建课程测验
     *
     * @param courseExam 测验信息
     * @param user 创建者
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
     * @param examId 测验ID
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
} 