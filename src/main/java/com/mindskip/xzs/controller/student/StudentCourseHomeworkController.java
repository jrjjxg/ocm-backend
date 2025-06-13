package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Homework;
import com.mindskip.xzs.domain.HomeworkAnswer;
import com.mindskip.xzs.domain.HomeworkQuestion;
import com.mindskip.xzs.domain.Question;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.question.QuestionObject;
import com.mindskip.xzs.domain.question.QuestionItemObject;
import com.mindskip.xzs.service.HomeworkService;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.service.QuestionService;
import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.viewmodel.student.homework.HomeworkAnswerVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * 学生端课程作业控制器 - 基于课程上下文的RESTful API
 * 使用Homework系统（题目型作业）
 */
@RestController("StudentCourseHomeworkController")
@RequestMapping(value = "/api/student/courses")
public class StudentCourseHomeworkController extends BaseApiController {

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private CourseStudentService courseStudentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TextContentService textContentService;

    /**
     * 获取课程作业列表 (RESTful风格 - 使用GET方法)
     */
    @GetMapping("/{courseId}/homework")
    public RestResponse<Map<String, Object>> getCourseHomework(@PathVariable Long courseId,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        List<Homework> homeworkList = homeworkService.getByCourseId(courseId.intValue());

        List<Map<String, Object>> allHomework = homeworkList.stream()
                .filter(homework -> homework.getStatus() != null && homework.getStatus() >= 2) // 只显示已发布的作业
                .map(homework -> {
                    Map<String, Object> vm = new HashMap<>();
                    vm.put("id", homework.getId());
                    vm.put("title", homework.getTitle());
                    vm.put("description", homework.getDescription());
                    vm.put("totalScore", homework.getTotalScore());
                    vm.put("startTime", DateTimeUtil.dateFormat(homework.getStartTime()));
                    vm.put("endTime", DateTimeUtil.dateFormat(homework.getEndTime()));
                    vm.put("createTime", DateTimeUtil.dateFormat(homework.getCreateTime()));

                    // 检查当前学生是否已提交该作业
                    HomeworkAnswer answer = homeworkService.getStudentAnswer(homework.getId(), currentUser.getId());
                    vm.put("submitted", answer != null);
                    if (answer != null) {
                        vm.put("submitTime", DateTimeUtil.dateFormat(answer.getSubmitTime()));
                        vm.put("score", answer.getScore());
                        vm.put("answerStatus", answer.getStatus()); // 1:未提交 2:已提交 3:已批改
                    }

                    // 设置作业状态
                    Date now = new Date();
                    String homeworkStatus;
                    if (now.before(homework.getStartTime())) {
                        homeworkStatus = "pending"; // 未开始
                    } else if (now.after(homework.getEndTime())) {
                        homeworkStatus = "overdue"; // 已截止
                    } else if (answer != null && answer.getStatus() != null && answer.getStatus() >= 2) {
                        homeworkStatus = "submitted"; // 已提交
                    } else {
                        homeworkStatus = "pending"; // 待完成
                    }
                    vm.put("status", homeworkStatus);

                    return vm;
                }).collect(Collectors.toList());

        // 应用筛选条件
        List<Map<String, Object>> filteredHomework = allHomework.stream()
                .filter(hw -> {
                    // 状态筛选
                    if (status != null && !status.isEmpty() && !status.equals(hw.get("status"))) {
                        return false;
                    }
                    // 关键词筛选
                    if (keyword != null && !keyword.isEmpty()) {
                        String title = (String) hw.get("title");
                        String description = (String) hw.get("description");
                        return (title != null && title.contains(keyword)) ||
                                (description != null && description.contains(keyword));
                    }
                    return true;
                }).collect(Collectors.toList());

        // 计算统计信息
        int totalHomework = filteredHomework.size();
        int completedHomework = (int) filteredHomework.stream()
                .filter(hw -> "submitted".equals(hw.get("status")))
                .count();

        // 计算平均分
        double averageScore = filteredHomework.stream()
                .filter(hw -> hw.get("score") != null)
                .mapToInt(hw -> (Integer) hw.get("score"))
                .average()
                .orElse(0.0) / 10; // 转换为十分制

        // 分页处理
        int total = filteredHomework.size();
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        List<Map<String, Object>> pagedHomework = filteredHomework.subList(startIndex, endIndex);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", pagedHomework);
        result.put("total", total);
        result.put("pageNum", pageIndex);
        result.put("pageSize", pageSize);
        result.put("totalHomework", totalHomework);
        result.put("completedHomework", completedHomework);
        result.put("averageScore", Math.round(averageScore * 100.0) / 100.0);

        return RestResponse.ok(result);
    }

    /**
     * 获取作业详情
     */
    @GetMapping("/{courseId}/homework/{homeworkId}")
    public RestResponse<Map<String, Object>> getHomeworkDetail(@PathVariable Long courseId,
            @PathVariable Long homeworkId) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId.intValue());

        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在");
        }

        Map<String, Object> vm = new HashMap<>();
        vm.put("id", homework.getId());
        vm.put("title", homework.getTitle());
        vm.put("description", homework.getDescription());
        vm.put("totalScore", homework.getTotalScore());
        vm.put("startTime", DateTimeUtil.dateFormat(homework.getStartTime()));
        vm.put("endTime", DateTimeUtil.dateFormat(homework.getEndTime()));
        vm.put("createTime", DateTimeUtil.dateFormat(homework.getCreateTime()));
        vm.put("timeLimit", homework.getTimeLimit());
        vm.put("attemptLimit", homework.getAttemptLimit());

        // 获取作业题目（包含完整题目信息）
        List<HomeworkQuestion> homeworkQuestions = homeworkService.getHomeworkQuestions(homework.getId());
        List<Map<String, Object>> questionDetails = new ArrayList<>();

        for (HomeworkQuestion hq : homeworkQuestions) {
            Question question = questionService.selectById(hq.getQuestionId());
            if (question != null) {
                Map<String, Object> questionMap = new HashMap<>();
                questionMap.put("id", question.getId());
                questionMap.put("questionType", question.getQuestionType());
                questionMap.put("score", hq.getScore()); // 使用作业中设置的分数
                questionMap.put("itemOrder", hq.getItemOrder());

                // 获取题目内容
                if (question.getInfoTextContentId() != null) {
                    TextContent textContent = textContentService.selectById(question.getInfoTextContentId());
                    if (textContent != null) {
                        questionMap.put("content", textContent.getContent());
                    }
                }

                // 获取题目选项（对于选择题）
                if (question.getQuestionType() == 1 || question.getQuestionType() == 2
                        || question.getQuestionType() == 3) { // 单选、多选或判断题
                    if (question.getInfoTextContentId() != null) {
                        TextContent textContent = textContentService.selectById(question.getInfoTextContentId());
                        if (textContent != null) {
                            try {
                                QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(),
                                        QuestionObject.class);
                                if (questionObject != null && questionObject.getQuestionItemObjects() != null) {
                                    List<Map<String, Object>> options = new ArrayList<>();
                                    for (QuestionItemObject itemObject : questionObject.getQuestionItemObjects()) {
                                        Map<String, Object> option = new HashMap<>();
                                        option.put("prefix", itemObject.getPrefix());
                                        option.put("content", itemObject.getContent());
                                        options.add(option);
                                    }
                                    questionMap.put("options", options);
                                }
                            } catch (Exception e) {
                                // 如果解析失败，记录日志但不影响主流程
                                System.err.println("解析题目选项失败，题目ID: " + question.getId() + ", 错误: " + e.getMessage());
                            }
                        }
                    }
                }

                questionDetails.add(questionMap);
            }
        }

        vm.put("questions", questionDetails);

        // 检查当前学生是否已提交该作业
        HomeworkAnswer answer = homeworkService.getStudentAnswer(homework.getId(), currentUser.getId());
        vm.put("submitted", answer != null);
        if (answer != null) {
            vm.put("submitTime", DateTimeUtil.dateFormat(answer.getSubmitTime()));
            vm.put("score", answer.getScore());
            vm.put("status", answer.getStatus());
        }

        return RestResponse.ok(vm);
    }

    /**
     * 开始作业
     */
    @PostMapping("/{courseId}/homework/{homeworkId}/start")
    public RestResponse<String> startHomework(@PathVariable Long courseId, @PathVariable Long homeworkId) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId.intValue());
        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 检查作业是否可以开始
        Date now = new Date();
        if (now.before(homework.getStartTime())) {
            return RestResponse.fail(400, "作业还未开始");
        }
        if (now.after(homework.getEndTime())) {
            return RestResponse.fail(400, "作业已截止");
        }

        return RestResponse.ok("可以开始作业");
    }

    /**
     * 提交作业答案
     */
    @PostMapping("/{courseId}/homework/{homeworkId}/submit")
    public RestResponse<String> submitHomework(@PathVariable Long courseId, @PathVariable Long homeworkId,
            @RequestBody @Valid HomeworkAnswerVM answerVM) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        Homework homework = homeworkService.selectById(homeworkId.intValue());
        if (homework == null || !homework.getCourseId().equals(courseId.intValue())) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 检查作业是否在提交时间内
        Date now = new Date();
        if (now.before(homework.getStartTime())) {
            return RestResponse.fail(400, "作业还未开始，不能提交");
        }
        if (now.after(homework.getEndTime())) {
            return RestResponse.fail(400, "作业已截止，不能提交");
        }

        // 设置提交信息
        answerVM.setHomeworkId(homeworkId.intValue());
        answerVM.setStudentId(currentUser.getId());
        answerVM.setStatus(2); // 已提交状态

        // 提交答案
        homeworkService.submitAnswer(answerVM);

        return RestResponse.ok("作业提交成功");
    }

    /**
     * 保存作业草稿
     */
    @PostMapping("/{courseId}/homework/{homeworkId}/save")
    public RestResponse<String> saveHomeworkDraft(@PathVariable Long courseId, @PathVariable Long homeworkId,
            @RequestBody @Valid HomeworkAnswerVM answerVM) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        // 设置草稿信息
        answerVM.setHomeworkId(homeworkId.intValue());
        answerVM.setStudentId(currentUser.getId());
        answerVM.setStatus(1); // 草稿状态

        // 保存草稿
        homeworkService.submitAnswer(answerVM);

        return RestResponse.ok("草稿保存成功");
    }

    /**
     * 获取作业结果
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/result")
    public RestResponse<Map<String, Object>> getHomeworkResult(@PathVariable Long courseId,
            @PathVariable Long homeworkId) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        HomeworkAnswer answer = homeworkService.getStudentAnswer(homeworkId.intValue(), currentUser.getId());

        if (answer == null) {
            return RestResponse.fail(404, "未找到提交记录");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("score", answer.getScore());
        result.put("status", answer.getStatus());
        result.put("submitTime", DateTimeUtil.dateFormat(answer.getSubmitTime()));

        return RestResponse.ok(result);
    }

    /**
     * 获取学生答题记录
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/answers")
    public RestResponse<List<HomeworkAnswer>> getStudentAnswers(@PathVariable Long courseId,
            @PathVariable Long homeworkId) {
        User currentUser = getCurrentUser();

        // 检查学生是否选修了该课程
        if (!courseStudentService.isStudentEnrolled(courseId, currentUser.getId())) {
            return RestResponse.fail(403, "未选修该课程");
        }

        List<HomeworkAnswer> answers = homeworkService.getStudentAnswers(homeworkId.intValue(), currentUser.getId());
        return RestResponse.ok(answers);
    }

    /**
     * 调试端点：查看作业题目的原始数据
     */
    @GetMapping("/{courseId}/homework/{homeworkId}/debug")
    public RestResponse<Map<String, Object>> debugHomeworkDetail(@PathVariable Long courseId,
            @PathVariable Long homeworkId) {
        User currentUser = getCurrentUser();

        Homework homework = homeworkService.selectById(homeworkId.intValue());
        if (homework == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        List<HomeworkQuestion> homeworkQuestions = homeworkService.getHomeworkQuestions(homework.getId());
        Map<String, Object> debugInfo = new HashMap<>();

        for (HomeworkQuestion hq : homeworkQuestions) {
            Question question = questionService.selectById(hq.getQuestionId());
            if (question != null) {
                Map<String, Object> questionDebug = new HashMap<>();
                questionDebug.put("questionId", question.getId());
                questionDebug.put("questionType", question.getQuestionType());
                questionDebug.put("infoTextContentId", question.getInfoTextContentId());

                if (question.getInfoTextContentId() != null) {
                    TextContent textContent = textContentService.selectById(question.getInfoTextContentId());
                    if (textContent != null) {
                        questionDebug.put("rawContent", textContent.getContent());

                        try {
                            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(),
                                    QuestionObject.class);
                            if (questionObject != null) {
                                questionDebug.put("parsedObject", questionObject);
                                questionDebug.put("titleContent", questionObject.getTitleContent());
                                questionDebug.put("questionItemObjects", questionObject.getQuestionItemObjects());
                            }
                        } catch (Exception e) {
                            questionDebug.put("parseError", e.getMessage());
                        }
                    }
                }

                debugInfo.put("question_" + question.getId(), questionDebug);
            }
        }

        return RestResponse.ok(debugInfo);
    }

}
