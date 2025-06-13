package com.mindskip.xzs.controller.teacher;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Homework;
import com.mindskip.xzs.domain.HomeworkAnswer;
import com.mindskip.xzs.domain.HomeworkQuestion;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.service.HomeworkService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkCreateVM;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.question.QuestionEditRequestVM;
import com.mindskip.xzs.service.QuestionService;
import com.mindskip.xzs.utility.ExamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * 教师端课程作业管理Controller - RESTful风格
 */
@RestController("TeacherCourseHomeworkController")
@RequestMapping("/api/teacher/courses")
public class TeacherCourseHomeworkController extends BaseApiController {

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private TeacherCourseService teacherCourseService;

    @Autowired
    private CourseStudentService courseStudentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private com.mindskip.xzs.service.TextContentService textContentService;

    /**
     * 获取课程作业列表 (RESTful风格 - 使用GET方法)
     */
    @GetMapping("/{courseId}/homework")
    public RestResponse<PageInfo<Homework>> getCourseHomework(@PathVariable Long courseId,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        HomeworkPageRequestVM requestVM = new HomeworkPageRequestVM();
        requestVM.setTeacherId(currentUser.getId());
        requestVM.setCourseId(courseId.intValue());
        requestVM.setPageIndex(pageIndex);
        requestVM.setPageSize(pageSize);

        PageInfo<Homework> pageInfo = homeworkService.page(requestVM);
        return RestResponse.ok(pageInfo);
    }

    /**
     * 创建课程作业 (RESTful风格)
     */
    @PostMapping("/{courseId}/homework")
    public RestResponse<?> createHomework(@PathVariable Long courseId, @RequestBody HomeworkCreateVM createVM) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限在该课程创建作业");
        }

        // 设置课程ID和创建者ID
        createVM.setCourseId(courseId.intValue());
        createVM.setTeacherId(currentUser.getId());

        try {
            homeworkService.createHomework(createVM);
            return RestResponse.ok("作业创建成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "创建作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程作业详情 (RESTful风格)
     */
    @GetMapping("/{courseId}/homework/{homeworkId}")
    public RestResponse<Map<String, Object>> getHomework(@PathVariable Long courseId,
            @PathVariable Integer homeworkId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        boolean hasPermission = teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId);
        if (!hasPermission) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在或不属于该课程");
        }

        // --- 开始组装返回数据 ---
        Map<String, Object> vm = new HashMap<>();
        vm.put("id", homework.getId());
        vm.put("title", homework.getTitle());
        vm.put("description", homework.getDescription());
        vm.put("courseName", "课程名称待获取"); // TODO: 获取课程名称
        vm.put("totalScore", homework.getTotalScore());
        vm.put("attemptLimit", homework.getAttemptLimit());
        vm.put("startTime", homework.getStartTime());
        vm.put("endTime", homework.getEndTime());
        vm.put("timeLimit", homework.getTimeLimit());
        vm.put("status", homework.getStatus());
        vm.put("createUser", homework.getCreateUser());

        // 获取并组装完整的题目信息
        List<HomeworkQuestion> homeworkQuestions = homeworkService.getHomeworkQuestions(homework.getId());
        List<QuestionEditRequestVM> questionVMs = new ArrayList<>();
        for (HomeworkQuestion hq : homeworkQuestions) {
            QuestionEditRequestVM questionVM = questionService.getQuestionEditRequestVM(hq.getQuestionId());
            if (questionVM != null) {
                questionVM.setScore(ExamUtil.scoreToVM(hq.getScore())); // 使用作业中为题目设定的分数
                questionVMs.add(questionVM);
            }
        }
        vm.put("questions", questionVMs);
        // --- 数据组装结束 ---

        return RestResponse.ok(vm);
    }

    /**
     * 更新课程作业 (RESTful风格)
     */
    @PutMapping("/{courseId}/homework/{homeworkId}")
    public RestResponse<?> updateHomework(@PathVariable Long courseId, @PathVariable Integer homeworkId,
            @RequestBody HomeworkCreateVM updateVM) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证作业是否属于该课程和当前教师
        if (!homework.getCourseId().equals(courseId.intValue())
                || !homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "没有权限修改该作业");
        }

        // 设置更新信息
        updateVM.setId(homeworkId);
        updateVM.setCourseId(courseId.intValue());
        updateVM.setTeacherId(currentUser.getId());

        try {
            homeworkService.updateHomework(updateVM);
            return RestResponse.ok("作业更新成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "更新作业失败: " + e.getMessage());
        }
    }

    /**
     * 删除课程作业 (RESTful风格)
     */
    @DeleteMapping("/{courseId}/homework/{homeworkId}")
    public RestResponse<?> deleteHomework(@PathVariable Long courseId, @PathVariable Integer homeworkId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证作业是否属于该课程和当前教师
        if (!homework.getCourseId().equals(courseId.intValue())
                || !homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "没有权限删除该作业");
        }

        try {
            homeworkService.deleteById(homeworkId);
            return RestResponse.ok("作业删除成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "删除作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程作业题目列表 (RESTful风格)
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/questions")
    public RestResponse<List<QuestionEditRequestVM>> getHomeworkQuestions(@PathVariable Long courseId,
            @PathVariable Integer homeworkId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证作业是否属于该课程
        if (!homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不属于该课程");
        }

        // 获取作业题目关联信息
        List<HomeworkQuestion> homeworkQuestions = homeworkService.getHomeworkQuestions(homeworkId);

        // 获取完整的题目信息
        List<QuestionEditRequestVM> questionDetails = new ArrayList<>();
        for (HomeworkQuestion hq : homeworkQuestions) {
            try {
                QuestionEditRequestVM questionVM = questionService.getQuestionEditRequestVM(hq.getQuestionId());
                if (questionVM != null) {
                    // 设置作业中的分数
                    questionVM.setScore(ExamUtil.scoreToVM(hq.getScore()));
                    questionDetails.add(questionVM);
                }
            } catch (Exception e) {
                System.err.println("获取题目详情失败，题目ID: " + hq.getQuestionId() + ", 错误: " + e.getMessage());
            }
        }

        return RestResponse.ok(questionDetails);
    }

    /**
     * 发布课程作业 (RESTful风格)
     */
    @PostMapping("/{courseId}/homework/{homeworkId}/publish")
    public RestResponse<?> publishHomework(@PathVariable Long courseId, @PathVariable Integer homeworkId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证作业是否属于该课程和当前教师
        if (!homework.getCourseId().equals(courseId.intValue())
                || !homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "没有权限发布该作业");
        }

        try {
            homeworkService.publishHomework(homeworkId);
            return RestResponse.ok("作业发布成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "发布作业失败: " + e.getMessage());
        }
    }

    /**
     * 结束课程作业 (RESTful风格)
     */
    @PostMapping("/{courseId}/homework/{homeworkId}/end")
    public RestResponse<?> endHomework(@PathVariable Long courseId, @PathVariable Integer homeworkId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证作业是否属于该课程和当前教师
        if (!homework.getCourseId().equals(courseId.intValue())
                || !homework.getCreateUser().equals(currentUser.getId())) {
            return RestResponse.fail(403, "没有权限结束该作业");
        }

        try {
            homeworkService.endHomework(homeworkId);
            return RestResponse.ok("作业已结束");
        } catch (Exception e) {
            return RestResponse.fail(500, "结束作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取课程作业统计信息 (RESTful风格)
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/statistics")
    public RestResponse<Map<String, Object>> getHomeworkStatistics(@PathVariable Long courseId,
            @PathVariable Integer homeworkId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId);
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证作业是否属于该课程
        if (!homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不属于该课程");
        }

        // 获取统计信息（现在会显示正确的课程学生数量）
        Object statisticsObj = homeworkService.getHomeworkStatistics(homeworkId);
        Map<String, Object> statistics = new HashMap<>();
        if (statisticsObj instanceof Map) {
            statistics = (Map<String, Object>) statisticsObj;
        }

        return RestResponse.ok(statistics);
    }

    /**
     * 获取作业的学生提交列表
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/submissions")
    public RestResponse<Map<String, Object>> getHomeworkSubmissions(@PathVariable Long courseId,
            @PathVariable Long homeworkId,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        User currentUser = getCurrentUser();

        // 验证权限
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId.intValue());
        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在");
        }

        try {
            // 获取课程的所有学生
            List<CourseStudentDTO> courseStudents = courseStudentService.getCourseStudents(courseId);

            // 获取该作业的所有答案记录
            List<HomeworkAnswer> allAnswers = homeworkService.getAnswersByHomeworkId(homeworkId.intValue());

            // 按学生ID分组答案
            Map<Integer, List<HomeworkAnswer>> answersByStudent = allAnswers.stream()
                    .collect(Collectors.groupingBy(HomeworkAnswer::getStudentId));

            List<Map<String, Object>> submissions = new ArrayList<>();

            for (CourseStudentDTO student : courseStudents) {
                Map<String, Object> submission = new HashMap<>();
                submission.put("studentId", student.getUserId());
                submission.put("studentName", student.getRealName());
                submission.put("studentNumber", student.getUserName());

                // 检查学生是否提交了作业
                List<HomeworkAnswer> studentAnswers = answersByStudent.get(student.getUserId());

                if (studentAnswers != null && !studentAnswers.isEmpty()) {
                    // 找到状态最高的答案（最新的提交状态）
                    HomeworkAnswer latestAnswer = studentAnswers.stream()
                            .max(Comparator.comparing(answer -> answer.getStatus() != null ? answer.getStatus() : 0))
                            .orElse(null);

                    if (latestAnswer != null && latestAnswer.getStatus() != null && latestAnswer.getStatus() >= 2) {
                        submission.put("submitted", true);
                        submission.put("submitTime", DateTimeUtil.dateFormat(latestAnswer.getSubmitTime()));
                        submission.put("status", latestAnswer.getStatus()); // 2:已提交 3:已批改

                        // 计算总分
                        int totalScore = studentAnswers.stream()
                                .filter(answer -> answer.getScore() != null)
                                .mapToInt(HomeworkAnswer::getScore)
                                .sum();
                        submission.put("score", totalScore);

                        if (latestAnswer.getStatus() >= 3) {
                            submission.put("gradeStatus", "graded");
                        } else {
                            submission.put("gradeStatus", "pending");
                        }
                    } else {
                        submission.put("submitted", false);
                        submission.put("gradeStatus", "not_submitted");
                    }
                } else {
                    submission.put("submitted", false);
                    submission.put("gradeStatus", "not_submitted");
                }

                submissions.add(submission);
            }

            // 应用筛选条件
            List<Map<String, Object>> filteredSubmissions = submissions.stream()
                    .filter(submission -> {
                        // 状态筛选
                        if (status != null && !status.isEmpty()) {
                            if ("submitted".equals(status) && !(Boolean) submission.get("submitted")) {
                                return false;
                            }
                            if ("pending".equals(status) && (Boolean) submission.get("submitted")) {
                                return false;
                            }
                            if ("graded".equals(status) && !"graded".equals(submission.get("gradeStatus"))) {
                                return false;
                            }
                        }
                        // 关键词筛选
                        if (keyword != null && !keyword.isEmpty()) {
                            String studentName = (String) submission.get("studentName");
                            String studentNumber = (String) submission.get("studentNumber");
                            return (studentName != null && studentName.contains(keyword)) ||
                                    (studentNumber != null && studentNumber.contains(keyword));
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            // 分页处理
            int total = filteredSubmissions.size();
            int start = (pageIndex - 1) * pageSize;
            int end = Math.min(start + pageSize, total);

            List<Map<String, Object>> pagedSubmissions = filteredSubmissions.subList(start, end);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pagedSubmissions);
            result.put("total", total);
            result.put("pageIndex", pageIndex);
            result.put("pageSize", pageSize);

            // 统计信息
            long submittedCount = submissions.stream().filter(s -> (Boolean) s.get("submitted")).count();
            long gradedCount = submissions.stream().filter(s -> "graded".equals(s.get("gradeStatus"))).count();

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalStudents", courseStudents.size());
            statistics.put("submittedCount", submittedCount);
            statistics.put("pendingCount", courseStudents.size() - submittedCount);
            statistics.put("gradedCount", gradedCount);
            result.put("statistics", statistics);

            return RestResponse.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.fail(500, "获取提交列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生作业答题详情
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/students/{studentId}/answers")
    public RestResponse<List<Map<String, Object>>> getStudentHomeworkAnswers(@PathVariable Long courseId,
            @PathVariable Long homeworkId,
            @PathVariable Integer studentId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证权限
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId.intValue());
        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在");
        }

        try {
            // 获取学生的答题记录
            List<HomeworkAnswer> studentAnswers = homeworkService.getStudentAnswers(homeworkId.intValue(), studentId);

            // 获取作业的题目信息
            List<HomeworkQuestion> homeworkQuestions = homeworkService.getHomeworkQuestions(homeworkId.intValue());

            List<Map<String, Object>> answerDetails = new ArrayList<>();

            for (HomeworkQuestion hq : homeworkQuestions) {
                Map<String, Object> answerDetail = new HashMap<>();

                // 获取题目信息
                com.mindskip.xzs.domain.Question question = questionService.selectById(hq.getQuestionId());
                if (question != null) {
                    answerDetail.put("questionId", question.getId());
                    answerDetail.put("questionType", question.getQuestionType());
                    answerDetail.put("questionScore", hq.getScore());
                    answerDetail.put("itemOrder", hq.getItemOrder());

                    // 获取题目内容
                    if (question.getInfoTextContentId() != null) {
                        com.mindskip.xzs.domain.TextContent textContent = textContentService
                                .selectById(question.getInfoTextContentId());
                        if (textContent != null) {
                            answerDetail.put("questionContent", textContent.getContent());
                        }
                    }
                }

                // 查找对应的学生答案
                HomeworkAnswer studentAnswer = studentAnswers.stream()
                        .filter(answer -> answer.getQuestionId().equals(hq.getQuestionId()))
                        .findFirst()
                        .orElse(null);

                if (studentAnswer != null) {
                    answerDetail.put("id", studentAnswer.getId());
                    answerDetail.put("answer", studentAnswer.getAnswer());
                    answerDetail.put("score", studentAnswer.getScore());
                    answerDetail.put("status", studentAnswer.getStatus());
                    answerDetail.put("submitTime", DateTimeUtil.dateFormat(studentAnswer.getSubmitTime()));
                    answerDetail.put("isCorrect", studentAnswer.getIsCorrect());
                } else {
                    answerDetail.put("id", null);
                    answerDetail.put("answer", null);
                    answerDetail.put("score", null);
                    answerDetail.put("status", 1); // 未答
                    answerDetail.put("submitTime", null);
                    answerDetail.put("isCorrect", null);
                }

                answerDetails.add(answerDetail);
            }

            return RestResponse.ok(answerDetails);

        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.fail(500, "获取学生答题详情失败: " + e.getMessage());
        }
    }

    /**
     * 手动评分API
     */
    @PostMapping("/{courseId}/homework/{homeworkId}/students/{studentId}/grade")
    public RestResponse<?> gradeStudentHomework(@PathVariable Long courseId,
            @PathVariable Long homeworkId,
            @PathVariable Integer studentId,
            @RequestBody Map<String, Object> gradeData) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证权限
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId.intValue());
        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在");
        }

        try {
            // 解析评分数据
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> answers = (List<Map<String, Object>>) gradeData.get("answers");

            if (answers != null) {
                for (Map<String, Object> answerData : answers) {
                    Integer answerId = (Integer) answerData.get("id");
                    Object scoreObj = answerData.get("score");
                    String feedback = (String) answerData.get("feedback");

                    if (answerId != null && scoreObj != null) {
                        Integer score = 0;
                        if (scoreObj instanceof Integer) {
                            score = (Integer) scoreObj;
                        } else if (scoreObj instanceof Double) {
                            score = ((Double) scoreObj).intValue();
                        } else if (scoreObj instanceof String) {
                            try {
                                score = Integer.parseInt((String) scoreObj);
                            } catch (NumberFormatException e) {
                                // 忽略无效分数
                                continue;
                            }
                        }

                        // 调用批改服务
                        homeworkService.manualGrade(answerId, score, feedback);
                    }
                }
            }

            return RestResponse.ok("评分成功");

        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.fail(500, "评分失败: " + e.getMessage());
        }
    }
}
