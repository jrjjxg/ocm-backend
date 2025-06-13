package com.mindskip.xzs.controller.teacher;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.service.CourseService;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.viewmodel.admin.course.CourseResponseVM;
import com.mindskip.xzs.viewmodel.admin.course.CourseStudentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("TeacherCourseController")
@RequestMapping(value = "/api/teacher/courses")
public class CourseController extends BaseApiController {

    private final CourseService courseService;
    private final TeacherCourseService teacherCourseService;
    private final CourseStudentService courseStudentService;

    @Autowired
    public CourseController(CourseService courseService, TeacherCourseService teacherCourseService,
            CourseStudentService courseStudentService) {
        this.courseService = courseService;
        this.teacherCourseService = teacherCourseService;
        this.courseStudentService = courseStudentService;
    }

    @GetMapping("")
    public RestResponse<List<CourseResponseVM>> list() {
        User currentUser = getCurrentUser();
        List<CourseResponseVM> courses = teacherCourseService.getTeacherCourses(currentUser.getId());
        return RestResponse.ok(courses);
    }

    @GetMapping("/{id}")
    public RestResponse<CourseResponseVM> select(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), id)) {
            return RestResponse.fail(403, "没有权限");
        }

        CourseResponseVM course = courseService.getCourseById(id);
        return RestResponse.ok(course);
    }

    @RequestMapping(value = "/students/{courseId}", method = RequestMethod.POST)
    public RestResponse<List<CourseStudentDTO>> students(@PathVariable Long courseId,
            @RequestBody(required = false) Map<String, Object> params) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        Integer pageIndex = params != null && params.containsKey("pageIndex") ? (Integer) params.get("pageIndex") : 1;
        Integer pageSize = params != null && params.containsKey("pageSize") ? (Integer) params.get("pageSize") : 10;
        String keyword = params != null && params.containsKey("keyword") ? (String) params.get("keyword") : null;

        List<CourseStudentDTO> students = courseService.getCourseStudents(courseId, pageIndex, pageSize, keyword);
        return RestResponse.ok(students);
    }

    /**
     * 获取课程学生列表（RESTful风格，支持分页）- GET方法
     * 前端调用路径：/api/teacher/courses/{courseId}/students
     */
    @GetMapping("/{courseId}/students")
    public RestResponse<PageInfo<CourseStudentDTO>> getCourseStudents(@PathVariable Long courseId,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            PageInfo<CourseStudentDTO> pageInfo = courseStudentService.getCourseStudentsPage(courseId, pageIndex,
                    pageSize, keyword);
            return RestResponse.ok(pageInfo);
        } catch (Exception e) {
            return RestResponse.fail(500, "获取学生列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程学生列表（RESTful风格，支持分页）- POST方法（向后兼容）
     * 前端调用路径：/api/teacher/courses/{courseId}/students
     */
    @PostMapping("/{courseId}/students")
    public RestResponse<PageInfo<CourseStudentDTO>> getCourseStudentsPost(@PathVariable Long courseId,
            @RequestBody(required = false) Map<String, Object> params) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        Integer pageIndex = params != null && params.containsKey("pageIndex") ? (Integer) params.get("pageIndex") : 1;
        Integer pageSize = params != null && params.containsKey("pageSize") ? (Integer) params.get("pageSize") : 10;
        String keyword = params != null && params.containsKey("keyword") ? (String) params.get("keyword") : null;

        PageInfo<CourseStudentDTO> pageInfo = courseStudentService.getCourseStudentsPage(courseId, pageIndex, pageSize,
                keyword);
        return RestResponse.ok(pageInfo);
    }

    /**
     * 更新学生成绩
     *
     * @param requestVM 更新成绩请求
     * @return 更新结果
     */
    @RequestMapping(value = "/student/score", method = RequestMethod.POST)
    public RestResponse<?> updateStudentScore(@RequestBody CourseStudentRequestVM requestVM) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), requestVM.getCourseId())) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            teacherCourseService.updateStudentScore(requestVM);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 添加学生到课程
     */
    @RequestMapping(value = "/student/add", method = RequestMethod.POST)
    public RestResponse<?> addStudentToCourse(@RequestBody Map<String, Object> params) {
        User currentUser = getCurrentUser();
        Long courseId = Long.valueOf(params.get("courseId").toString());
        List<Integer> studentIds = (List<Integer>) params.get("studentIds");

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            int addedCount = teacherCourseService.addStudentsToCourse(courseId, studentIds);
            return RestResponse.ok(addedCount);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 从课程中移除学生
     */
    @RequestMapping(value = "/student/remove", method = RequestMethod.POST)
    public RestResponse<?> removeStudentFromCourse(@RequestBody Map<String, Object> params) {
        User currentUser = getCurrentUser();
        Long courseId = Long.valueOf(params.get("courseId").toString());
        Integer studentId = (Integer) params.get("studentId");

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            boolean result = teacherCourseService.removeStudentFromCourse(courseId, studentId);
            return RestResponse.ok(result);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }
}