package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.repository.CourseStudentMapper;
import com.mindskip.xzs.service.CourseStudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 课程-学生关联服务实现类
 */
@Service
public class CourseStudentServiceImpl extends BaseServiceImpl<CourseStudent> implements CourseStudentService {
    
    private final CourseStudentMapper courseStudentMapper;
    
    @Autowired
    public CourseStudentServiceImpl(CourseStudentMapper courseStudentMapper) {
        super(courseStudentMapper);
        this.courseStudentMapper = courseStudentMapper;
    }
    
    @Override
    public List<CourseStudentDTO> getCourseStudents(Long courseId) {
        return courseStudentMapper.getCourseStudents(courseId);
    }
    
    @Override
    public PageInfo<CourseStudentDTO> getCourseStudentsPage(Long courseId, Integer pageIndex, Integer pageSize, String keyword) {
        return PageHelper.startPage(pageIndex, pageSize).doSelectPageInfo(() -> 
            courseStudentMapper.getCourseStudentsWithPage(courseId, keyword)
        );
    }
    
    @Override
    public Integer getStudentCountByCourseId(Long courseId) {
        List<CourseStudentDTO> students = courseStudentMapper.getCourseStudents(courseId);
        return students != null ? students.size() : 0;
    }
    
    @Override
    public Boolean addStudentToCourse(Long courseId, Integer studentId, Integer creatorId) {
        // 检查是否已经存在
        CourseStudent existStudent = courseStudentMapper.selectByCourseIdAndStudentId(courseId, studentId);
        if (existStudent != null) {
            // 如果已存在但状态为已退，则更新状态为已选
            if (existStudent.getStatus() == 2) {
                existStudent.setStatus(1);
                existStudent.setUpdateTime(new Date());
                courseStudentMapper.updateByPrimaryKeySelective(existStudent);
                return true;
            }
            return false; // 已经选课
        }
        
        // 创建新的选课记录
        Date now = new Date();
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseId(courseId);
        courseStudent.setStudentId(studentId);
        courseStudent.setStatus(1); // 已选
        courseStudent.setCreatorId(creatorId);
        courseStudent.setCreateTime(now);
        courseStudent.setUpdateTime(now);
        
        return courseStudentMapper.insertSelective(courseStudent) > 0;
    }
    
    @Override
    public Boolean removeStudentFromCourse(Long courseId, Integer studentId) {
        CourseStudent courseStudent = courseStudentMapper.selectByCourseIdAndStudentId(courseId, studentId);
        if (courseStudent == null) {
            return false;
        }
        
        // 更新状态为已退
        return courseStudentMapper.updateStatus(courseId, studentId, 2) > 0;
    }
    
    @Override
    public Boolean updateStudentScore(Long courseId, Integer studentId, Float score) {
        CourseStudent courseStudent = courseStudentMapper.selectByCourseIdAndStudentId(courseId, studentId);
        if (courseStudent == null) {
            return false;
        }
        
        // 更新成绩
        return courseStudentMapper.updateFinalScore(courseId, studentId, score) > 0;
    }
    
    @Override
    public CourseStudent selectById(Long id) {
        return courseStudentMapper.selectByPrimaryKeyLong(id);
    }
    
    @Override
    public int deleteById(Long id) {
        return courseStudentMapper.deleteByPrimaryKeyLong(id);
    }
} 