package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.domain.Question;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.question.QuestionObject;
import com.mindskip.xzs.domain.question.QuestionItemObject;
import com.mindskip.xzs.service.ExamPaperService;
import com.mindskip.xzs.service.QuestionService;
import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperTitleItemVM;
import com.mindskip.xzs.viewmodel.admin.question.QuestionEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.question.QuestionEditItemVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学生端试卷控制器
 */
@RestController("StudentExamPaperController")
@RequestMapping(value = "/api/student")
public class ExamPaperController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(ExamPaperController.class);

    @Autowired
    private ExamPaperService examPaperService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TextContentService textContentService;

    /**
     * 获取试卷详情
     */
    @GetMapping("/exam-papers/{id}")
    public RestResponse<ExamPaperEditRequestVM> getExamPaper(@PathVariable Integer id) {
        try {
            User currentUser = getCurrentUser();

            // 获取试卷详情
            ExamPaperEditRequestVM examPaper = examPaperService.examPaperToVM(id);

            if (examPaper == null) {
                return RestResponse.fail(404, "试卷不存在");
            }

            // 处理题目列表，确保选项正确解析
            if (examPaper.getTitleItems() != null) {
                for (ExamPaperTitleItemVM titleItem : examPaper.getTitleItems()) {
                    if (titleItem.getQuestionItems() != null) {
                        for (QuestionEditRequestVM questionVM : titleItem.getQuestionItems()) {
                            // 确保题目选项被正确解析
                            ensureQuestionOptionsAreParsed(questionVM);
                        }
                    }
                }
            }

            return RestResponse.ok(examPaper);

        } catch (Exception e) {
            logger.error("获取试卷详情失败: examPaperId={}, userId={}, error={}",
                    id, getCurrentUser().getId(), e.getMessage(), e);
            return RestResponse.fail(500, "获取试卷详情失败，请稍后重试");
        }
    }

    /**
     * 确保题目选项被正确解析
     */
    private void ensureQuestionOptionsAreParsed(QuestionEditRequestVM questionVM) {
        try {
            // 对于选择题和判断题，确保选项被正确解析
            if (questionVM.getQuestionType() == 1 || questionVM.getQuestionType() == 2
                    || questionVM.getQuestionType() == 3) {
                // 如果选项为空或者内容不正确，重新从数据库解析
                if (questionVM.getItems() == null || questionVM.getItems().isEmpty() ||
                        questionVM.getItems().stream()
                                .anyMatch(item -> item.getContent() == null || item.getContent().trim().isEmpty())) {

                    Question question = questionService.selectById(questionVM.getId());
                    if (question != null && question.getInfoTextContentId() != null) {
                        TextContent textContent = textContentService.selectById(question.getInfoTextContentId());
                        if (textContent != null) {
                            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(),
                                    QuestionObject.class);
                            if (questionObject != null && questionObject.getQuestionItemObjects() != null) {
                                // 重新设置正确的选项
                                questionVM.setItems(questionObject.getQuestionItemObjects().stream()
                                        .map(itemObject -> {
                                            QuestionEditItemVM itemVM = new QuestionEditItemVM();
                                            itemVM.setPrefix(itemObject.getPrefix());
                                            itemVM.setContent(itemObject.getContent());
                                            itemVM.setScore("1"); // 默认分数
                                            return itemVM;
                                        })
                                        .collect(Collectors.toList()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("解析题目选项失败，题目ID: {}, 错误: {}", questionVM.getId(), e.getMessage());
        }
    }
}