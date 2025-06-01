package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Course;
import com.mindskip.xzs.domain.Resource;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.service.CourseService;
import com.mindskip.xzs.service.ResourceService;
import com.mindskip.xzs.service.StudentCourseService;
import com.mindskip.xzs.viewmodel.admin.course.CourseResponseVM;
import com.mindskip.xzs.viewmodel.admin.course.CourseStudentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("StudentCourseController")
@RequestMapping(value = "/api/student/courses")
public class CourseController extends BaseApiController {

    private final CourseService courseService;
    private final StudentCourseService studentCourseService;
    private final ResourceService resourceService;

    @Autowired
    public CourseController(CourseService courseService, StudentCourseService studentCourseService, ResourceService resourceService) {
        this.courseService = courseService;
        this.studentCourseService = studentCourseService;
        this.resourceService = resourceService;
    }

    /**
     * 获取所有可选课程
     *
     * @return 课程列表
     */
    @GetMapping("/available")
    public RestResponse<List<CourseResponseVM>> getAvailableCourses() {
        User currentUser = getCurrentUser();
        List<CourseResponseVM> courses = studentCourseService.getAvailableCourses(currentUser.getId());
        return RestResponse.ok(courses);
    }

    /**
     * 获取学生已选课程
     *
     * @return 课程列表
     */
    @GetMapping
    public RestResponse<List<CourseResponseVM>> getEnrolledCourses() {
        User currentUser = getCurrentUser();
        List<CourseResponseVM> courses = studentCourseService.getStudentCourses(currentUser.getId());
        return RestResponse.ok(courses);
    }

    /**
     * 查看课程详情
     *
     * @param id 课程ID
     * @return 课程详情
     */
    @GetMapping("/{id}")
    public RestResponse<CourseResponseVM> getCourse(@PathVariable Long id) {
        CourseResponseVM course = courseService.getCourseById(id);
        if (course == null) {
            return RestResponse.fail(404, "课程不存在");
        }
        return RestResponse.ok(course);
    }

    /**
     * 学生选课
     *
     * @param courseId 课程ID
     * @return 选课结果
     */
    @PostMapping("/{courseId}/enroll")
    public RestResponse<?> enrollCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        
        CourseStudentRequestVM requestVM = new CourseStudentRequestVM();
        requestVM.setCourseId(courseId);
        requestVM.setStudentId(currentUser.getId());
        
        try {
            courseService.enrollStudent(requestVM, currentUser.getId());
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 学生退课
     *
     * @param courseId 课程ID
     * @return 退课结果
     */
    @DeleteMapping("/{courseId}/enroll")
    public RestResponse<?> unenrollCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        
        CourseStudentRequestVM requestVM = new CourseStudentRequestVM();
        requestVM.setCourseId(courseId);
        requestVM.setStudentId(currentUser.getId());
        
        try {
            courseService.unenrollStudent(requestVM);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 获取课程资源
     *
     * @param courseId 课程ID
     * @return 课程资源列表
     */
    @GetMapping("/{courseId}/resources")
    public RestResponse<List<Resource>> getCourseResources(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        // 检查学生是否已选修该课程
        boolean isEnrolled = studentCourseService.isStudentEnrolledInCourse(currentUser.getId(), courseId);
        if (!isEnrolled) {
            return RestResponse.fail(403, "您未选修该课程，无法访问资源");
        }
        
        List<Resource> resources = resourceService.getResourcesByCourseId(courseId);
        return RestResponse.ok(resources);
    }
} 