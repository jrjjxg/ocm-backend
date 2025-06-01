package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.service.ExamPaperAnswerService;
import com.mindskip.xzs.service.ExamPaperService;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.PageInfoHelper;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperPageResponseVM;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("StudentExamPaperController")
@RequestMapping(value = "/api/student/exam-papers")
public class ExamPaperController extends BaseApiController {

    private final ExamPaperService examPaperService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ExamPaperController(ExamPaperService examPaperService, ExamPaperAnswerService examPaperAnswerService, ApplicationEventPublisher eventPublisher) {
        this.examPaperService = examPaperService;
        this.examPaperAnswerService = examPaperAnswerService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 获取试卷详情
     * 
     * @param id 试卷ID
     * @return 试卷详情
     */
    @GetMapping("/{id}")
    public RestResponse<ExamPaperEditRequestVM> getExamPaper(@PathVariable Integer id) {
        ExamPaperEditRequestVM vm = examPaperService.examPaperToVM(id);
        return RestResponse.ok(vm);
    }

    /**
     * 获取试卷列表
     * 
     * @param model 分页查询条件
     * @return 试卷列表
     */
    @PostMapping
    public RestResponse<PageInfo<ExamPaperPageResponseVM>> listExamPapers(@RequestBody @Valid ExamPaperPageVM model) {
        PageInfo<ExamPaper> pageInfo = examPaperService.studentPage(model);
        PageInfo<ExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperPageResponseVM vm = modelMapper.map(e, ExamPaperPageResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }
}
