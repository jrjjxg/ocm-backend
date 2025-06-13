package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.CourseExamService;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.viewmodel.student.StudentExamResponseVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生端课程测验控制器
 */
@RestController("StudentCourseExamController")
@RequestMapping(value = "/api/student/courses")
public class StudentCourseExamController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(StudentCourseExamController.class);

    @Autowired
    private CourseExamService courseExamService;

    @Autowired
    private CourseStudentService courseStudentService;

    /**
     * 获取课程测验列表（支持分页和查询）
     */
    @GetMapping("/{courseId}/exams")
    public RestResponse<Map<String, Object>> getExamList(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {

        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            // 获取所有测验
            List<StudentExamResponseVM> allExams = courseExamService.getCourseExamsForStudent(courseId,
                    currentUser.getId());

            // 如果没有测验，返回空列表
            if (allExams == null || allExams.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("list", allExams);
                result.put("total", 0);
                result.put("pageNum", pageIndex);
                result.put("pageSize", pageSize);
                return RestResponse.ok(result);
            }

            // 应用筛选条件
            List<StudentExamResponseVM> filteredExams = allExams.stream()
                    .filter(exam -> {
                        // 状态筛选
                        if (status != null && !status.isEmpty()) {
                            String examStatus = getExamStatusString(exam.getStatus());
                            if (!status.equals(examStatus)) {
                                return false;
                            }
                        }

                        // 关键词筛选
                        if (keyword != null && !keyword.isEmpty()) {
                            return (exam.getTitle() != null && exam.getTitle().contains(keyword)) ||
                                    (exam.getDescription() != null && exam.getDescription().contains(keyword));
                        }

                        return true;
                    })
                    .collect(Collectors.toList());

            // 分页处理
            int total = filteredExams.size();
            int startIndex = (pageIndex - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, total);

            List<StudentExamResponseVM> pagedExams;
            if (startIndex >= total) {
                pagedExams = Collections.emptyList(); // 空列表
            } else {
                pagedExams = filteredExams.subList(startIndex, endIndex);
            }

            // 构建响应结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", pagedExams);
            result.put("total", total);
            result.put("pageNum", pageIndex);
            result.put("pageSize", pageSize);

            return RestResponse.ok(result);

        } catch (Exception e) {
            // 记录错误日志
            logger.error("获取课程测验列表失败: courseId={}, userId={}, error={}",
                    courseId, getCurrentUser().getId(), e.getMessage(), e);

            // 返回友好的错误信息
            if (e.getMessage().contains("未选修该课程")) {
                return RestResponse.fail(403, "未选修该课程");
            } else if (e.getMessage().contains("课程不存在")) {
                return RestResponse.fail(404, "课程不存在");
            } else {
                return RestResponse.fail(500, "获取测验列表失败，请稍后重试");
            }
        }
    }

    /**
     * 将数字状态转换为字符串状态
     */
    private String getExamStatusString(Integer status) {
        if (status == null)
            return "unknown";
        switch (status) {
            case 1:
                return "not_started";
            case 2:
                return "in_progress";
            case 3:
                return "ended";
            case 4:
                return "cancelled";
            default:
                return "unknown";
        }
    }

    /**
     * 获取课程测验详情
     */
    @GetMapping("/{courseId}/exams/{examId}")
    public RestResponse<StudentExamResponseVM> getExamDetail(@PathVariable Long courseId, @PathVariable Long examId) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            StudentExamResponseVM examDetail = courseExamService.getCourseExamDetailForStudent(courseId, examId,
                    currentUser.getId());
            if (examDetail == null) {
                return RestResponse.fail(404, "测验不存在");
            }

            return RestResponse.ok(examDetail);

        } catch (Exception e) {
            logger.error("获取测验详情失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "获取测验详情失败，请稍后重试");
        }
    }

    /**
     * 开始测验
     */
    @PostMapping("/{courseId}/exams/{examId}/start")
    public RestResponse<Integer> startExam(@PathVariable Long courseId, @PathVariable Long examId) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            // 检查是否可以参加测验
            if (!courseExamService.canStudentTakeExam(courseId, examId, currentUser.getId())) {
                return RestResponse.fail(400, "无法参加测验");
            }

            Integer examPaperAnswerId = courseExamService.startExam(courseId, examId, currentUser.getId());
            return RestResponse.ok(examPaperAnswerId);

        } catch (Exception e) {
            logger.error("开始测验失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "开始测验失败：" + e.getMessage());
        }
    }

    /**
     * 提交测验
     */
    @PostMapping("/{courseId}/exams/{examId}/submit")
    public RestResponse submitExam(@PathVariable Long courseId, @PathVariable Long examId,
            @RequestParam Integer examPaperAnswerId) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            courseExamService.submitExam(courseId, examId, currentUser.getId(), examPaperAnswerId);
            return RestResponse.ok();

        } catch (Exception e) {
            logger.error("提交测验失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "提交测验失败：" + e.getMessage());
        }
    }

    /**
     * 获取测验结果
     */
    @GetMapping("/{courseId}/exams/{examId}/result")
    public RestResponse<Object> getExamResult(@PathVariable Long courseId, @PathVariable Long examId) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            Object result = courseExamService.getExamResultForStudent(courseId, examId, currentUser.getId());
            return RestResponse.ok(result);

        } catch (Exception e) {
            logger.error("获取测验结果失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "获取测验结果失败：" + e.getMessage());
        }
    }

    /**
     * 保存测验答案草稿
     */
    @PostMapping("/{courseId}/exams/{examId}/save")
    public RestResponse<String> saveExamDraft(@PathVariable Long courseId, @PathVariable Long examId,
            @RequestBody Object draftData) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            courseExamService.saveExamDraft(courseId, examId, currentUser.getId(), draftData);
            return RestResponse.ok("草稿保存成功");

        } catch (Exception e) {
            logger.error("保存草稿失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "保存草稿失败：" + e.getMessage());
        }
    }

    /**
     * 获取学生的答题记录
     */
    @GetMapping("/{courseId}/exams/{examId}/answers")
    public RestResponse<Object> getStudentExamAnswers(@PathVariable Long courseId, @PathVariable Long examId) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.fail(403, "未选修该课程");
            }

            Object answers = courseExamService.getStudentExamAnswers(courseId, examId, currentUser.getId());
            return RestResponse.ok(answers);

        } catch (Exception e) {
            logger.error("获取答题记录失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "获取答题记录失败：" + e.getMessage());
        }
    }

    /**
     * 检查是否可以参加测验
     */
    @GetMapping("/{courseId}/exams/{examId}/can-take")
    public RestResponse<Boolean> canTakeExam(@PathVariable Long courseId, @PathVariable Long examId) {
        try {
            User currentUser = getCurrentUser();

            // 检查学生是否选修了该课程
            if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
                return RestResponse.ok(false);
            }

            boolean canTake = courseExamService.canStudentTakeExam(courseId, examId, currentUser.getId());
            return RestResponse.ok(canTake);

        } catch (Exception e) {
            logger.error("检查测验权限失败: courseId={}, examId={}, userId={}, error={}",
                    courseId, examId, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.ok(false);
        }
    }
}
