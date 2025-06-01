package com.mindskip.xzs.service;

import com.mindskip.xzs.viewmodel.admin.course.CourseResponseVM;
import com.mindskip.xzs.viewmodel.admin.course.CourseStudentRequestVM;

import java.util.List;

/**
 * 教师课程服务接口
 */
public interface TeacherCourseService {
    /**
     * 获取教师教授的课程列表
     *
     * @param teacherId 教师ID
     * @return 课程列表
     */
    List<CourseResponseVM> getTeacherCourses(Integer teacherId);

    /**
     * 验证课程是否由该教师教授
     *
     * @param teacherId 教师ID
     * @param courseId 课程ID
     * @return 如果教师教授该课程则返回true，否则返回false
     */
    boolean validateTeacherCourse(Integer teacherId, Long courseId);
    
    /**
     * 更新学生成绩
     *
     * @param requestVM 更新成绩请求
     */
    void updateStudentScore(CourseStudentRequestVM requestVM);
    
    /**
     * 创建教师与课程的关联关系
     *
     * @param teacherId 教师ID
     * @param courseId 课程ID
     * @return 创建成功返回true，否则返回false
     */
    boolean createTeacherCourseRelation(Integer teacherId, Long courseId);
    
    /**
     * 添加学生到课程
     *
     * @param courseId 课程ID
     * @param studentIds 学生ID列表
     * @return 成功添加的学生数量
     */
    int addStudentsToCourse(Long courseId, List<Integer> studentIds);
    
    /**
     * 添加班级所有学生到课程
     *
     * @param courseId 课程ID
     * @param classId 班级ID
     * @return 成功添加的学生数量
     */
    int addClassStudentsToCourse(Long courseId, Integer classId);
    
    /**
     * 从课程中移除学生
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 移除成功返回true，否则返回false
     */
    boolean removeStudentFromCourse(Long courseId, Integer studentId);
} 