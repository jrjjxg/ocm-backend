package com.mindskip.xzs.controller.teacher;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.domain.ExamPaperAnswer;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.ExamPaperAnswerService;
import com.mindskip.xzs.service.ExamPaperService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamResponseVM;
import com.mindskip.xzs.viewmodel.teacher.exam.CourseExamRequestVM;
import com.mindskip.xzs.viewmodel.teacher.exam.CourseExamResponseVM;
import com.mindskip.xzs.viewmodel.teacher.exam.ExamResultResponseVM;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.PageInfoHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController("TeacherExamController")
@RequestMapping(value = "/api/teacher/courses")
@SuppressWarnings("unchecked")
public class ExamController extends BaseApiController {

    private final ExamPaperService examPaperService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final TeacherCourseService teacherCourseService;

    @Autowired
    public ExamController(ExamPaperService examPaperService,
            ExamPaperAnswerService examPaperAnswerService,
            TeacherCourseService teacherCourseService) {
        this.examPaperService = examPaperService;
        this.examPaperAnswerService = examPaperAnswerService;
        this.teacherCourseService = teacherCourseService;
    }

    /**
     * 获取课程测验列表
     *
     * @param courseId 课程ID
     * @return 测验列表
     */
    @GetMapping("/{courseId}/exams")
    public RestResponse<List<CourseExamResponseVM>> getExamList(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        // 获取课程相关的考试列表
        List<CourseExamResponseVM> exams = examPaperService.getCourseExams(courseId);
        return RestResponse.ok(exams);
    }

    /**
     * 获取测验详情
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 测验详情
     */
    @GetMapping("/{courseId}/exams/{examId}")
    public RestResponse<CourseExamResponseVM> getExam(@PathVariable Long courseId, @PathVariable Integer examId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        // 获取测验详情
        CourseExamResponseVM exam = examPaperService.getCourseExam(courseId, examId);
        if (exam == null) {
            return RestResponse.fail(404, "测验不存在");
        }
        return RestResponse.ok(exam);
    }

    /**
     * 创建新测验
     *
     * @param courseId 课程ID
     * @param model    测验信息
     * @return 创建结果
     */
    @PostMapping("/{courseId}/exams")
    public RestResponse createExam(@PathVariable Long courseId, @RequestBody @Valid CourseExamRequestVM model) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        // 设置课程ID和创建者
        model.setCourseId(courseId);

        try {
            examPaperService.saveCourseExam(model, currentUser);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 更新测验
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @param model    测验信息
     * @return 更新结果
     */
    @PutMapping("/{courseId}/exams/{examId}")
    public RestResponse updateExam(@PathVariable Long courseId, @PathVariable Integer examId,
            @RequestBody @Valid CourseExamRequestVM model) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        // 设置课程ID和测验ID
        model.setCourseId(courseId);
        model.setId(examId);

        try {
            examPaperService.updateCourseExam(model, currentUser);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 删除测验
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 删除结果
     */
    @DeleteMapping("/{courseId}/exams/{examId}")
    public RestResponse deleteExam(@PathVariable Long courseId, @PathVariable Integer examId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            examPaperService.deleteCourseExam(courseId, examId);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 获取测验成绩
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 成绩列表
     */
    @GetMapping("/{courseId}/exams/{examId}/results")
    public RestResponse<List<ExamResultResponseVM>> getExamResults(@PathVariable Long courseId,
            @PathVariable Integer examId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        List<ExamResultResponseVM> results = examPaperAnswerService.getExamResults(courseId, examId);
        return RestResponse.ok(results);
    }

    /**
     * 获取可用试卷列表
     *
     * @param courseId 课程ID
     * @return 试卷列表
     */
    @GetMapping("/{courseId}/papers")
    public RestResponse<List<ExamResponseVM>> getPaperList(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        List<ExamPaper> examPapers = examPaperService.getAvailablePapers(currentUser.getId());
        List<ExamResponseVM> paperVMs = examPapers.stream().map(paper -> {
            ExamResponseVM vm = new ExamResponseVM();
            vm.setId(paper.getId());
            vm.setName(paper.getName());
            return vm;
        }).collect(Collectors.toList());

        return RestResponse.ok(paperVMs);
    }

    /**
     * 获取试卷分页列表
     *
     * @param courseId  课程ID
     * @param requestVM 分页查询条件
     * @return 试卷分页列表
     */
    @PostMapping("/{courseId}/papers/page")
    public RestResponse<PageInfo<ExamResponseVM>> getPaperPage(@PathVariable Long courseId,
            @RequestBody @Valid ExamPaperPageRequestVM requestVM) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        // 设置当前教师ID，只查询该教师创建的试卷
        requestVM.setCreateUser(currentUser.getId());

        try {
            PageInfo<ExamPaper> pageInfo = examPaperService.teacherPage(requestVM);
            PageInfo<ExamResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
                ExamResponseVM vm = new ExamResponseVM();
                vm.setId(e.getId());
                vm.setName(e.getName());
                vm.setQuestionCount(e.getQuestionCount());
                vm.setScore(e.getScore());
                vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
                vm.setCreateUser(e.getCreateUser());
                vm.setSubjectId(e.getSubjectId());
                vm.setPaperType(e.getPaperType());
                return vm;
            });
            return RestResponse.ok(page);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 创建试卷
     *
     * @param courseId  课程ID
     * @param requestVM 试卷信息
     * @return 创建结果
     */
    @PostMapping("/{courseId}/papers")
    public RestResponse<ExamPaperEditRequestVM> createPaper(@PathVariable Long courseId,
            @RequestBody @Valid ExamPaperEditRequestVM requestVM) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            ExamPaper examPaper = examPaperService.savePaperFromVM(requestVM, currentUser);
            ExamPaperEditRequestVM newVM = examPaperService.examPaperToVM(examPaper.getId());
            return RestResponse.ok(newVM);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 获取试卷详情
     *
     * @param courseId 课程ID
     * @param paperId  试卷ID
     * @return 试卷详情
     */
    @GetMapping("/{courseId}/papers/{paperId}")
    public RestResponse<ExamPaperEditRequestVM> getPaper(@PathVariable Long courseId, @PathVariable Integer paperId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            // 先检查试卷是否属于当前教师
            ExamPaper examPaper = examPaperService.selectById(paperId);
            if (examPaper == null || examPaper.getDeleted()) {
                return RestResponse.fail(404, "试卷不存在");
            }
            if (!examPaper.getCreateUser().equals(currentUser.getId())) {
                return RestResponse.fail(403, "没有权限访问该试卷");
            }

            ExamPaperEditRequestVM vm = examPaperService.examPaperToVM(paperId);
            return RestResponse.ok(vm);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 更新试卷
     *
     * @param courseId  课程ID
     * @param paperId   试卷ID
     * @param requestVM 试卷信息
     * @return 更新结果
     */
    @PutMapping("/{courseId}/papers/{paperId}")
    public RestResponse<ExamPaperEditRequestVM> updatePaper(@PathVariable Long courseId, @PathVariable Integer paperId,
            @RequestBody @Valid ExamPaperEditRequestVM requestVM) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            // 设置试卷ID
            requestVM.setId(paperId);
            ExamPaper examPaper = examPaperService.savePaperFromVM(requestVM, currentUser);
            ExamPaperEditRequestVM newVM = examPaperService.examPaperToVM(examPaper.getId());
            return RestResponse.ok(newVM);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 删除试卷
     *
     * @param courseId 课程ID
     * @param paperId  试卷ID
     * @return 删除结果
     */
    @DeleteMapping("/{courseId}/papers/{paperId}")
    public RestResponse<Void> deletePaper(@PathVariable Long courseId, @PathVariable Integer paperId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            // 验证试卷是否属于当前教师
            ExamPaper examPaper = examPaperService.selectById(paperId);
            if (examPaper == null || examPaper.getDeleted()) {
                return RestResponse.fail(404, "试卷不存在");
            }
            if (!examPaper.getCreateUser().equals(currentUser.getId())) {
                return RestResponse.fail(403, "没有权限删除该试卷");
            }

            // 软删除试卷
            examPaper.setDeleted(true);
            examPaperService.updateByIdFilter(examPaper);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 获取测验统计数据
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @return 统计数据
     */
    @GetMapping("/{courseId}/exams/{examId}/statistics")
    public RestResponse<Object> getStatistics(@PathVariable Long courseId, @PathVariable Integer examId) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            Object statistics = examPaperAnswerService.getExamStatistics(courseId, examId);
            return RestResponse.ok(statistics);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 获取测验答卷列表
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @param query    查询条件
     * @return 答卷列表
     */
    @PostMapping("/{courseId}/exams/{examId}/answers")
    public RestResponse<Object> getExamAnswers(@PathVariable Long courseId, @PathVariable Integer examId,
            @RequestBody Object query) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            Object answers = examPaperAnswerService.getExamAnswers(courseId, examId, query);
            return RestResponse.ok(answers);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 评阅测验答卷
     *
     * @param courseId 课程ID
     * @param examId   测验ID
     * @param answerId 答卷ID
     * @param data     评阅数据
     * @return 评阅结果
     */
    @PutMapping("/{courseId}/exams/{examId}/answers/{answerId}")
    public RestResponse<Object> evaluateAnswer(@PathVariable Long courseId, @PathVariable Integer examId,
            @PathVariable Integer answerId, @RequestBody Object data) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        try {
            examPaperAnswerService.evaluateAnswer(courseId, examId, answerId, data);
            return RestResponse.ok();
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }
}