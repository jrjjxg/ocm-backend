package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.domain.CourseStudent;
import com.mindskip.xzs.domain.CourseTeacher;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.repository.CourseMapper;
import com.mindskip.xzs.repository.CourseStudentMapper;
import com.mindskip.xzs.repository.CourseTeacherMapper;
import com.mindskip.xzs.repository.UserMapper;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.service.enums.CourseStatusEnum;
import com.mindskip.xzs.domain.enums.UserStatusEnum;
import com.mindskip.xzs.viewmodel.admin.course.CourseResponseVM;
import com.mindskip.xzs.viewmodel.admin.course.CourseStudentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {

    private final CourseTeacherMapper courseTeacherMapper;
    private final CourseMapper courseMapper;
    private final CourseStudentMapper courseStudentMapper;
    private final UserMapper userMapper;

    @Autowired
    public TeacherCourseServiceImpl(CourseTeacherMapper courseTeacherMapper, CourseMapper courseMapper,
            CourseStudentMapper courseStudentMapper, UserMapper userMapper) {
        this.courseTeacherMapper = courseTeacherMapper;
        this.courseMapper = courseMapper;
        this.courseStudentMapper = courseStudentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<CourseResponseVM> getTeacherCourses(Integer teacherId) {
        // 使用新添加的方法获取教师关联的课程ID
        List<Long> courseIds = courseTeacherMapper.findCourseIdsByTeacherId(teacherId);

        if (courseIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取课程详情并转换为CourseResponseVM
        List<CourseResponseVM> result = new ArrayList<>();
        for (Long courseId : courseIds) {
            Course course = courseMapper.selectByPrimaryKey(courseId);
            if (course != null && course.getStatus().equals(CourseStatusEnum.ENABLE.getCode())) {
                CourseResponseVM vm = CourseResponseVM.from(course);
                result.add(vm);
            }
        }

        return result;
    }

    @Override
    public boolean validateTeacherCourse(Integer teacherId, Long courseId) {
        CourseTeacher courseTeacher = courseTeacherMapper.selectByCourseIdAndTeacherId(courseId, teacherId);
        return courseTeacher != null;
    }

    @Override
    @Transactional
    public void updateStudentScore(CourseStudentRequestVM requestVM) {
        // 验证课程和学生关联是否存在
        if (courseStudentMapper.selectByCourseIdAndStudentId(requestVM.getCourseId(),
                requestVM.getStudentId()) == null) {
            throw new RuntimeException("该学生未选择此课程");
        }

        // 更新学生成绩
        courseStudentMapper.updateFinalScore(requestVM.getCourseId(), requestVM.getStudentId(),
                requestVM.getFinalScore());
    }

    @Override
    @Transactional
    public boolean createTeacherCourseRelation(Integer teacherId, Long courseId) {
        try {
            // 检查关联是否已存在
            if (validateTeacherCourse(teacherId, courseId)) {
                return true; // 关联已存在，视为创建成功
            }

            // 创建新的课程教师关联
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setCourseId(courseId);
            courseTeacher.setTeacherId(teacherId);
            courseTeacher.setAuthType(1); // 假设1表示课程创建者权限
            courseTeacher.setCreatorId(teacherId); // 创建者即为教师本人
            courseTeacher.setCreateTime(new Date());
            courseTeacher.setUpdateTime(new Date());

            // 保存到数据库
            courseTeacherMapper.insert(courseTeacher);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public int addStudentsToCourse(Long courseId, List<Integer> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return 0;
        }

        int addedCount = 0;
        Date now = new Date();

        for (Integer studentId : studentIds) {
            // 验证学生是否存在且有效
            User student = userMapper.selectByPrimaryKey(studentId);
            if (student == null || !student.getStatus().equals(UserStatusEnum.Enable.getCode())) {
                continue;
            }

            // 检查是否已经添加过
            CourseStudent existRecord = courseStudentMapper.selectByCourseIdAndStudentId(courseId, studentId);
            if (existRecord != null) {
                if (existRecord.getStatus() != 1) {
                    // 如果学生之前退课了，现在重新添加，只需要更新状态
                    courseStudentMapper.updateStatus(courseId, studentId, 1);
                    addedCount++;
                }
                continue;
            }

            // 创建新的课程学生关联
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setCourseId(courseId);
            courseStudent.setStudentId(studentId);
            courseStudent.setStatus(1); // 已选
            courseStudent.setFinalScore(null); // 初始成绩为null
            courseStudent.setCreatorId(null); // 可以设置为当前操作的教师ID
            courseStudent.setCreateTime(now);
            courseStudent.setUpdateTime(now);

            // 保存到数据库
            courseStudentMapper.insert(courseStudent);
            addedCount++;
        }

        return addedCount;
    }

    @Override
    @Transactional
    public int addClassStudentsToCourse(Long courseId, Integer classId) {
        // 获取班级学生列表
        List<User> classStudents = userMapper.selectByClassId(classId);
        if (classStudents == null || classStudents.isEmpty()) {
            return 0;
        }

        // 提取学生ID
        List<Integer> studentIds = new ArrayList<>();
        for (User student : classStudents) {
            studentIds.add(student.getId());
        }

        // 调用批量添加学生方法
        return addStudentsToCourse(courseId, studentIds);
    }

    @Override
    @Transactional
    public boolean removeStudentFromCourse(Long courseId, Integer studentId) {
        // 验证课程和学生关联是否存在
        CourseStudent courseStudent = courseStudentMapper.selectByCourseIdAndStudentId(courseId, studentId);
        if (courseStudent == null) {
            return false;
        }

        // 更新状态为已退出
        int result = courseStudentMapper.updateStatus(courseId, studentId, 2);
        return result > 0;
    }
}