package com.mindskip.xzs.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.domain.dto.CourseTeacherDTO;
import com.mindskip.xzs.service.CourseService;
import com.mindskip.xzs.utility.PageInfoHelper;
import com.mindskip.xzs.viewmodel.admin.course.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("AdminCourseController")
@RequestMapping(value = "/api/admin/course")
public class CourseController extends BaseApiController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/page/list")
    public RestResponse<PageInfo<CourseResponseVM>> pageList(@RequestBody CoursePageRequestVM model) {
        PageInfo<Course> pageInfo = courseService.page(model);
        PageInfo<CourseResponseVM> page = PageInfoHelper.copyMap(pageInfo, CourseResponseVM::from);
        return RestResponse.ok(page);
    }

    @GetMapping("/select/{id}")
    public RestResponse<CourseResponseVM> select(@PathVariable Long id) {
        Course course = courseService.selectById(id);
        CourseResponseVM vm = CourseResponseVM.from(course);
        return RestResponse.ok(vm);
    }

    @PostMapping("/edit")
    public RestResponse<CourseResponseVM> edit(@RequestBody @Valid CourseEditRequestVM model) {
        User currentUser = getCurrentUser();
        Course course = courseService.edit(model, currentUser.getId());
        CourseResponseVM vm = CourseResponseVM.from(course);
        return RestResponse.ok(vm);
    }

    @PutMapping("/changeStatus/{id}")
    public RestResponse<Integer> changeStatus(@PathVariable Long id) {
        Integer newStatus = courseService.changeStatus(id);
        return RestResponse.ok(newStatus);
    }

    @DeleteMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Long id) {
        courseService.delete(id);
        return RestResponse.ok();
    }

    @GetMapping("/{courseId}/teachers")
    public RestResponse<List<CourseTeacherDTO>> getCourseTeachers(@PathVariable Long courseId) {
        List<CourseTeacherDTO> teachers = courseService.getCourseTeachers(courseId);
        return RestResponse.ok(teachers);
    }

    @PostMapping("/teacher/assign")
    public RestResponse assignTeacher(@RequestBody @Valid CourseTeacherRequestVM model) {
        User currentUser = getCurrentUser();
        courseService.assignTeacher(model, currentUser.getId());
        return RestResponse.ok();
    }

    @PostMapping("/teacher/revoke")
    public RestResponse revokeTeacher(@RequestBody @Valid CourseTeacherRequestVM model) {
        courseService.revokeTeacher(model);
        return RestResponse.ok();
    }

    @GetMapping("/{courseId}/students")
    public RestResponse<List<CourseStudentDTO>> getCourseStudents(@PathVariable Long courseId) {
        List<CourseStudentDTO> students = courseService.getCourseStudents(courseId);
        return RestResponse.ok(students);
    }

    @PostMapping("/student/enroll")
    public RestResponse enrollStudent(@RequestBody @Valid CourseStudentRequestVM model) {
        User currentUser = getCurrentUser();
        courseService.enrollStudent(model, currentUser.getId());
        return RestResponse.ok();
    }

    @PostMapping("/student/unenroll")
    public RestResponse unenrollStudent(@RequestBody @Valid CourseStudentRequestVM model) {
        courseService.unenrollStudent(model);
        return RestResponse.ok();
    }
}