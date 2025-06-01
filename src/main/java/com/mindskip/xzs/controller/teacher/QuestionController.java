package com.mindskip.xzs.controller.teacher;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.base.SystemCode;
import com.mindskip.xzs.domain.Question;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.enums.QuestionTypeEnum;
import com.mindskip.xzs.domain.question.QuestionObject;
import com.mindskip.xzs.service.QuestionService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.utility.*;
import com.mindskip.xzs.viewmodel.admin.question.QuestionEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.question.QuestionPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.question.QuestionResponseVM;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController("TeacherQuestionController")
@RequestMapping(value = "/api/teacher/courses")
public class QuestionController extends BaseApiController {

    private final QuestionService questionService;
    private final TextContentService textContentService;
    private final TeacherCourseService teacherCourseService;

    @Autowired
    public QuestionController(QuestionService questionService, TextContentService textContentService, TeacherCourseService teacherCourseService) {
        this.questionService = questionService;
        this.textContentService = textContentService;
        this.teacherCourseService = teacherCourseService;
    }

    /**
     * 获取课程题目列表
     *
     * @param courseId 课程ID
     * @param requestBody 查询条件
     * @return 题目列表
     */
    @RequestMapping(value = "/{courseId}/questions", method = RequestMethod.POST)
    public RestResponse<PageInfo<QuestionResponseVM>> pageList(@PathVariable Long courseId, @RequestBody QuestionPageRequestVM requestBody) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }
        
        // 设置课程ID
        requestBody.setCourseId(courseId.intValue());
        
        PageInfo<Question> pageInfo = questionService.page(requestBody);
        PageInfo<QuestionResponseVM> page = PageInfoHelper.copyMap(pageInfo, q -> {
            QuestionResponseVM vm = modelMapper.map(q, QuestionResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(q.getCreateTime()));
            vm.setScore(ExamUtil.scoreToVM(q.getScore()));
            TextContent textContent = textContentService.selectById(q.getInfoTextContentId());
            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(), QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            return vm;
        });
        return RestResponse.ok(page);
    }

    /**
     * 编辑题目
     *
     * @param courseId 课程ID
     * @param model 题目信息
     * @return 操作结果
     */
    @RequestMapping(value = "/{courseId}/questions/edit", method = RequestMethod.POST)
    public RestResponse edit(@PathVariable Long courseId, @RequestBody @Valid QuestionEditRequestVM model) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }
        
        // 设置课程ID
        model.setCourseId(courseId.intValue());
        
        RestResponse validQuestionEditRequestResult = validQuestionEditRequestVM(model);
        if (validQuestionEditRequestResult.getCode() != SystemCode.OK.getCode()) {
            return validQuestionEditRequestResult;
        }

        if (null == model.getId()) {
            questionService.insertFullQuestion(model, getCurrentUser().getId());
        } else {
            questionService.updateFullQuestion(model);
        }

        return RestResponse.ok();
    }

    /**
     * 获取题目详情
     *
     * @param courseId 课程ID
     * @param id 题目ID
     * @return 题目详情
     */
    @RequestMapping(value = "/{courseId}/questions/{id}", method = RequestMethod.POST)
    public RestResponse<QuestionEditRequestVM> select(@PathVariable Long courseId, @PathVariable Integer id) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }
        
        QuestionEditRequestVM newVM = questionService.getQuestionEditRequestVM(id);
        return RestResponse.ok(newVM);
    }

    /**
     * 删除题目
     *
     * @param courseId 课程ID
     * @param id 题目ID
     * @return 操作结果
     */
    @RequestMapping(value = "/{courseId}/questions/{id}/delete", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Long courseId, @PathVariable Integer id) {
        User currentUser = getCurrentUser();
        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }
        
        Question question = questionService.selectById(id);
        if (question == null) {
            return RestResponse.fail(404, "题目不存在");
        }
        
        question.setDeleted(true);
        questionService.updateByIdFilter(question);
        return RestResponse.ok();
    }
    
    /**
     * 验证题目请求参数
     */
    private RestResponse validQuestionEditRequestVM(QuestionEditRequestVM model) {
        int qType = model.getQuestionType().intValue();
        boolean requireCorrect = qType == QuestionTypeEnum.SingleChoice.getCode() || qType == QuestionTypeEnum.TrueFalse.getCode();
        if (requireCorrect) {
            if (StringUtils.isBlank(model.getCorrect())) {
                String errorMsg = ErrorUtil.parameterErrorFormat("correct", "不能为空");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
            }
        }

        if (qType == QuestionTypeEnum.GapFilling.getCode()) {
            Integer fillSumScore = model.getItems().stream().mapToInt(d -> ExamUtil.scoreFromVM(d.getScore())).sum();
            Integer questionScore = ExamUtil.scoreFromVM(model.getScore());
            if (!fillSumScore.equals(questionScore)) {
                String errorMsg = ErrorUtil.parameterErrorFormat("score", "空分数和与题目总分不相等");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
            }
        }
        return RestResponse.ok();
    }
} 