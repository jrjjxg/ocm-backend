package com.mindskip.xzs.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.domain.dto.CourseTeacherDTO;
import com.mindskip.xzs.viewmodel.admin.course.*;

import java.util.List;

public interface CourseService {
    /**
     * 课程分页查询
     *
     * @param requestVM 查询条件
     * @return PageInfo<Course>
     */
    PageInfo<Course> page(CoursePageRequestVM requestVM);

    /**
     * 根据ID查询课程
     *
     * @param id 课程ID
     * @return Course
     */
    Course selectById(Long id);
    
    /**
     * 根据ID查询课程并转换为VM
     *
     * @param id 课程ID
     * @return CourseResponseVM
     */
    CourseResponseVM getCourseById(Long id);

    /**
     * 根据课程代码查询课程
     *
     * @param code 课程代码
     * @return Course
     */
    Course selectByCode(String code);

    /**
     * 创建或更新课程
     *
     * @param requestVM 课程信息
     * @return Course
     */
    Course edit(CourseEditRequestVM requestVM, Integer userId);
    
    /**
     * 创建新课程
     *
     * @param course 课程信息
     * @return 创建的课程
     */
    Course createCourse(Course course);

    /**
     * 更新课程状态
     *
     * @param id 课程ID
     * @return Integer
     */
    Integer changeStatus(Long id);

    /**
     * 删除课程
     *
     * @param id 课程ID
     */
    void delete(Long id);

    /**
     * 获取课程关联的教师列表
     *
     * @param courseId 课程ID
     * @return List<CourseTeacherDTO>
     */
    List<CourseTeacherDTO> getCourseTeachers(Long courseId);

    /**
     * 为课程分配教师
     *
     * @param requestVM 分配信息
     */
    void assignTeacher(CourseTeacherRequestVM requestVM, Integer userId);

    /**
     * 撤销教师的课程权限
     *
     * @param requestVM 撤销信息
     */
    void revokeTeacher(CourseTeacherRequestVM requestVM);

    /**
     * 获取课程关联的学生列表
     *
     * @param courseId 课程ID
     * @return List<CourseStudentDTO>
     */
    List<CourseStudentDTO> getCourseStudents(Long courseId);
    
    /**
     * 获取课程关联的学生列表（带分页和关键词搜索）
     *
     * @param courseId 课程ID
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @param keyword 搜索关键词
     * @return List<CourseStudentDTO>
     */
    List<CourseStudentDTO> getCourseStudents(Long courseId, Integer pageIndex, Integer pageSize, String keyword);

    /**
     * 为学生注册课程
     *
     * @param requestVM 注册信息
     */
    void enrollStudent(CourseStudentRequestVM requestVM, Integer userId);

    /**
     * 取消学生的课程注册
     *
     * @param requestVM 取消注册信息
     */
    void unenrollStudent(CourseStudentRequestVM requestVM);
} 