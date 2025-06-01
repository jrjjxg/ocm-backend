package com.mindskip.xzs.service;

import com.mindskip.xzs.viewmodel.admin.course.CourseResponseVM;

import java.util.List;

/**
 * 学生课程服务接口
 */
public interface StudentCourseService {
    /**
     * 获取学生可选的课程列表
     *
     * @param studentId 学生ID
     * @return 课程列表
     */
    List<CourseResponseVM> getAvailableCourses(Integer studentId);

    /**
     * 获取学生已选的课程列表
     *
     * @param studentId 学生ID
     * @return 课程列表
     */
    List<CourseResponseVM> getStudentCourses(Integer studentId);
    
    /**
     * 检查学生是否已选修特定课程
     *
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @return 是否已选修
     */
    boolean isStudentEnrolledInCourse(Integer studentId, Long courseId);
} 