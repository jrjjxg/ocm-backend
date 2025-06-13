package com.mindskip.xzs.controller.teacher;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Homework;
import com.mindskip.xzs.domain.HomeworkAnswer;
import com.mindskip.xzs.domain.HomeworkQuestion;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.HomeworkService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkCreateVM;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师端作业管理Controller
 */
@RestController
@RequestMapping("/api/teacher/homework")
public class HomeworkController extends BaseApiController {

    private final HomeworkService homeworkService;
    private final TeacherCourseService teacherCourseService;

    @Autowired
    public HomeworkController(HomeworkService homeworkService, TeacherCourseService teacherCourseService) {
        this.homeworkService = homeworkService;
        this.teacherCourseService = teacherCourseService;
    }

    /**
     * 分页查询作业列表
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<Homework>> page(@RequestBody HomeworkPageRequestVM requestVM) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 设置当前教师ID，只查询自己的作业
        requestVM.setTeacherId(currentUser.getId());

        PageInfo<Homework> pageInfo = homeworkService.page(requestVM);
        return RestResponse.ok(pageInfo);
    }

    /**
     * 创建作业
     */
    @PostMapping("/create")
    public RestResponse<?> create(@RequestBody HomeworkCreateVM createVM) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), createVM.getCourseId().longValue())) {
            return RestResponse.fail(403, "没有权限创建该课程的作业");
        }

        // 设置创建者ID
        createVM.setTeacherId(currentUser.getId());

        try {
            homeworkService.createHomework(createVM);
            return RestResponse.ok("作业创建成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "创建作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取作业详情（包含题目）
     */
    @GetMapping("/{id}")
    public RestResponse<Homework> detail(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.getHomeworkWithQuestions(id);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限：只有作业创建者或课程教师可以查看
        if (!homework.getCreateUser().equals(currentUser.getId()) &&
                !teacherCourseService.validateTeacherCourse(currentUser.getId(), homework.getCourseId().longValue())) {
            return RestResponse.fail(403, "没有权限查看该作业");
        }

        return RestResponse.ok(homework);
    }

    /**
     * 获取作业题目列表
     */
    @GetMapping("/{id}/questions")
    public RestResponse<List<HomeworkQuestion>> questions(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.selectById(id);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限
        if (!homework.getCreateUser().equals(currentUser.getId()) &&
                !teacherCourseService.validateTeacherCourse(currentUser.getId(), homework.getCourseId().longValue())) {
            return RestResponse.fail(403, "没有权限查看该作业题目");
        }

        List<HomeworkQuestion> questions = homeworkService.getHomeworkQuestions(id);
        return RestResponse.ok(questions);
    }

    /**
     * 发布作业
     */
    @PostMapping("/{id}/publish")
    public RestResponse<?> publish(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.selectById(id);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限：只有作业创建者可以发布
        if (!homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "只有作业创建者可以发布");
        }

        try {
            homeworkService.publishHomework(id);
            return RestResponse.ok("作业发布成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "发布作业失败: " + e.getMessage());
        }
    }

    /**
     * 结束作业
     */
    @PostMapping("/{id}/end")
    public RestResponse<?> end(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.selectById(id);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限：只有作业创建者可以结束
        if (!homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "只有作业创建者可以结束");
        }

        try {
            homeworkService.endHomework(id);
            return RestResponse.ok("作业已结束");
        } catch (Exception e) {
            return RestResponse.fail(500, "结束作业失败: " + e.getMessage());
        }
    }

    /**
     * 删除作业
     */
    @DeleteMapping("/{id}")
    public RestResponse<?> delete(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.selectById(id);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限：只有作业创建者可以删除
        if (!homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "只有作业创建者可以删除");
        }

        try {
            homeworkService.deleteById(id);
            return RestResponse.ok("作业删除成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "删除作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取作业统计信息
     */
    @GetMapping("/{id}/statistics")
    public RestResponse<Object> statistics(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.selectById(id);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限
        if (!homework.getCreateUser().equals(currentUser.getId()) &&
                !teacherCourseService.validateTeacherCourse(currentUser.getId(), homework.getCourseId().longValue())) {
            return RestResponse.fail(403, "没有权限查看该作业统计");
        }

        Object statistics = homeworkService.getHomeworkStatistics(id);
        return RestResponse.ok(statistics);
    }

    /**
     * 获取学生作业答题情况
     */
    @GetMapping("/{homeworkId}/student/{studentId}/answers")
    public RestResponse<List<HomeworkAnswer>> studentAnswers(@PathVariable Integer homeworkId,
            @PathVariable Integer studentId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证权限
        if (!homework.getCreateUser().equals(currentUser.getId()) &&
                !teacherCourseService.validateTeacherCourse(currentUser.getId(), homework.getCourseId().longValue())) {
            return RestResponse.fail(403, "没有权限查看该学生答题情况");
        }

        List<HomeworkAnswer> answers = homeworkService.getStudentAnswers(homeworkId, studentId);
        return RestResponse.ok(answers);
    }

    /**
     * 人工评分
     */
    @PostMapping("/answer/{answerId}/grade")
    public RestResponse<?> manualGrade(@PathVariable Integer answerId, @RequestBody GradeRequest gradeRequest) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        try {
            homeworkService.manualGrade(answerId, gradeRequest.getScore(), gradeRequest.getFeedback());
            return RestResponse.ok("评分成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "评分失败: " + e.getMessage());
        }
    }

    /**
     * 获取作业仪表板统计数据
     */
    @GetMapping("/dashboard/statistics")
    public RestResponse<Object> dashboardStatistics() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        try {
            Object dashboardStats = homeworkService.getDashboardStatistics(currentUser.getId());
            return RestResponse.ok(dashboardStats);
        } catch (Exception e) {
            return RestResponse.fail(500, "获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取待批改作业列表
     */
    @PostMapping("/pending/page")
    public RestResponse<PageInfo<Homework>> pendingPage(@RequestBody HomeworkPageRequestVM requestVM) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 设置当前教师ID和待批改状态
        requestVM.setTeacherId(currentUser.getId());
        requestVM.setPendingGrade(true);

        PageInfo<Homework> pageInfo = homeworkService.page(requestVM);
        return RestResponse.ok(pageInfo);
    }

    /**
     * 评分请求DTO
     */
    public static class GradeRequest {
        private Integer score;
        private String feedback;

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }
}
