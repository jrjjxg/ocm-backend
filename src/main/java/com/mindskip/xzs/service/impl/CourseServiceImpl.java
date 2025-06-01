package com.mindskip.xzs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.domain.CourseTeacher;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.domain.dto.CourseTeacherDTO;
import com.mindskip.xzs.repository.CourseMapper;
import com.mindskip.xzs.repository.CourseStudentMapper;
import com.mindskip.xzs.repository.CourseTeacherMapper;
import com.mindskip.xzs.service.CourseService;
import com.mindskip.xzs.viewmodel.admin.course.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseTeacherMapper courseTeacherMapper;
    private final CourseStudentMapper courseStudentMapper;

    @Autowired
    public CourseServiceImpl(CourseMapper courseMapper, CourseTeacherMapper courseTeacherMapper, CourseStudentMapper courseStudentMapper) {
        this.courseMapper = courseMapper;
        this.courseTeacherMapper = courseTeacherMapper;
        this.courseStudentMapper = courseStudentMapper;
    }

    @Override
    public PageInfo<Course> page(CoursePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> courseMapper.page(requestVM.getName()));
    }

    @Override
    public Course selectById(Long id) {
        return courseMapper.selectByPrimaryKey(id);
    }

    @Override
    public CourseResponseVM getCourseById(Long id) {
        Course course = this.selectById(id);
        return course == null ? null : CourseResponseVM.from(course);
    }

    @Override
    public Course selectByCode(String code) {
        return courseMapper.selectByCode(code);
    }

    /**
     * 创建或更新课程
     *
     * @param requestVM 课程信息
     * @return Course
     */
    @Override
    @Transactional
    public Course edit(CourseEditRequestVM requestVM, Integer userId) {
        // 验证课程代码唯一性
        Course existCourse = courseMapper.selectByCode(requestVM.getCode());
        if (existCourse != null && !existCourse.getId().equals(requestVM.getId())) {
            throw new RuntimeException("课程代码已存在");
        }
        
        Date now = new Date();
        Course course;
        
        if (requestVM.getId() == null) { // 新建课程
            course = new Course();
            course.setName(requestVM.getName());
            course.setCode(requestVM.getCode());
            course.setDescription(requestVM.getDescription());
            course.setSemester(requestVM.getSemester());
            course.setCredit(requestVM.getCredit());
            course.setStatus(requestVM.getStatus());
            course.setCreatorId(userId);
            course.setCreateTime(now);
            course.setUpdateTime(now);
            courseMapper.insert(course);
        } else { // 更新课程
            course = courseMapper.selectByPrimaryKey(requestVM.getId());
            if (course == null) {
                throw new RuntimeException("课程不存在");
            }
            course.setName(requestVM.getName());
            course.setCode(requestVM.getCode());
            course.setDescription(requestVM.getDescription());
            course.setSemester(requestVM.getSemester());
            course.setCredit(requestVM.getCredit());
            course.setStatus(requestVM.getStatus());
            course.setUpdateTime(now);
            courseMapper.updateByPrimaryKey(course);
        }
        
        return course;
    }

    @Override
    public Integer changeStatus(Long id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        Integer newStatus = course.getStatus() == 1 ? 0 : 1;
        courseMapper.updateStatus(id, newStatus);
        return newStatus;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Course course = courseMapper.selectByPrimaryKey(id);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        courseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeachers(Long courseId) {
        return courseTeacherMapper.getCourseTeachers(courseId);
    }

    @Override
    @Transactional
    public void assignTeacher(CourseTeacherRequestVM requestVM, Integer userId) {
        Course course = courseMapper.selectByPrimaryKey(requestVM.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        CourseTeacher existRecord = courseTeacherMapper.selectByCourseIdAndTeacherId(requestVM.getCourseId(), requestVM.getTeacherId());
        if (existRecord != null) {
            throw new RuntimeException("该教师已分配到此课程");
        }

        Date now = new Date();
        CourseTeacher courseTeacher = new CourseTeacher();
        courseTeacher.setCourseId(requestVM.getCourseId());
        courseTeacher.setTeacherId(requestVM.getTeacherId());
        courseTeacher.setAuthType(requestVM.getAuthType() != null ? requestVM.getAuthType() : 1);
        courseTeacher.setCreatorId(userId);
        courseTeacher.setCreateTime(now);
        courseTeacher.setUpdateTime(now);
        courseTeacherMapper.insert(courseTeacher);
    }

    @Override
    @Transactional
    public void revokeTeacher(CourseTeacherRequestVM requestVM) {
        courseTeacherMapper.deleteByCourseIdAndTeacherId(requestVM.getCourseId(), requestVM.getTeacherId());
    }

    @Override
    public List<CourseStudentDTO> getCourseStudents(Long courseId) {
        return courseStudentMapper.getCourseStudents(courseId);
    }
    
    @Override
    public List<CourseStudentDTO> getCourseStudents(Long courseId, Integer pageIndex, Integer pageSize, String keyword) {
        PageHelper.startPage(pageIndex, pageSize);
        return courseStudentMapper.getCourseStudentsWithPage(courseId, keyword);
    }

    @Override
    @Transactional
    public void enrollStudent(CourseStudentRequestVM requestVM, Integer userId) {
        Course course = courseMapper.selectByPrimaryKey(requestVM.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        CourseStudent existRecord = courseStudentMapper.selectByCourseIdAndStudentId(requestVM.getCourseId(), requestVM.getStudentId());
        if (existRecord != null) {
            if (existRecord.getStatus() == 1) {
                throw new RuntimeException("该学生已注册此课程");
            } else {
                // 如果学生之前退课了，现在重新注册，只需要更新状态
                courseStudentMapper.updateStatus(requestVM.getCourseId(), requestVM.getStudentId(), 1);
                return;
            }
        }

        Date now = new Date();
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseId(requestVM.getCourseId());
        courseStudent.setStudentId(requestVM.getStudentId());
        courseStudent.setStatus(1); // 已选
        courseStudent.setCreatorId(userId);
        courseStudent.setCreateTime(now);
        courseStudent.setUpdateTime(now);
        courseStudentMapper.insert(courseStudent);
    }

    @Override
    @Transactional
    public void unenrollStudent(CourseStudentRequestVM requestVM) {
        courseStudentMapper.updateStatus(requestVM.getCourseId(), requestVM.getStudentId(), 2); // 已退
    }

    /**
     * 创建新课程
     *
     * @param course 课程信息
     * @return 创建的课程
     */
    @Override
    @Transactional
    public Course createCourse(Course course) {
        // 验证课程代码唯一性
        Course existCourse = courseMapper.selectByCode(course.getCode());
        if (existCourse != null) {
            throw new RuntimeException("课程代码已存在");
        }
        
        // 插入课程记录
        courseMapper.insert(course);
        return course;
    }
} 