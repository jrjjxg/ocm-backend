package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.repository.CourseMapper;
import com.mindskip.xzs.repository.CourseStudentMapper;
import com.mindskip.xzs.service.StudentCourseService;
import com.mindskip.xzs.service.enums.CourseStatusEnum;
import com.mindskip.xzs.viewmodel.admin.course.CourseResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    private final CourseStudentMapper courseStudentMapper;
    private final CourseMapper courseMapper;

    @Autowired
    public StudentCourseServiceImpl(CourseStudentMapper courseStudentMapper, CourseMapper courseMapper) {
        this.courseStudentMapper = courseStudentMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<CourseResponseVM> getAvailableCourses(Integer studentId) {
        // 获取所有启用状态的课程
        List<Course> allCourses = courseMapper.page(null).stream()
                .filter(c -> c.getStatus().equals(CourseStatusEnum.ENABLE.getCode()))
                .collect(Collectors.toList());
        
        if (allCourses.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取学生已选课程IDs
        List<Long> enrolledCourseIds = courseStudentMapper.findCourseIdsByStudentId(studentId);
        
        // 过滤出未选课程并转换为VM
        return allCourses.stream()
                .filter(c -> !enrolledCourseIds.contains(c.getId()))
                .map(CourseResponseVM::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseVM> getStudentCourses(Integer studentId) {
        // 获取学生已选课程IDs
        List<Long> enrolledCourseIds = courseStudentMapper.findCourseIdsByStudentId(studentId);
        
        if (enrolledCourseIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取课程详情并转换为VM
        List<CourseResponseVM> result = new ArrayList<>();
        for (Long courseId : enrolledCourseIds) {
            Course course = courseMapper.selectByPrimaryKey(courseId);
            if (course != null && course.getStatus().equals(CourseStatusEnum.ENABLE.getCode())) {
                CourseResponseVM vm = CourseResponseVM.from(course);
                result.add(vm);
            }
        }
        
        return result;
    }
    
    @Override
    public boolean isStudentEnrolledInCourse(Integer studentId, Long courseId) {
        // 获取学生已选课程IDs
        List<Long> enrolledCourseIds = courseStudentMapper.findCourseIdsByStudentId(studentId);
        
        // 检查课程ID是否在已选课程列表中
        return enrolledCourseIds.contains(courseId);
    }
} 