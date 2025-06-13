package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.*;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.domain.enums.ExamPaperTypeEnum;
import com.mindskip.xzs.domain.exam.ExamPaperQuestionItemObject;
import com.mindskip.xzs.domain.exam.ExamPaperTitleItemObject;
import com.mindskip.xzs.domain.other.KeyValue;
import com.mindskip.xzs.repository.ExamPaperMapper;
import com.mindskip.xzs.repository.QuestionMapper;
import com.mindskip.xzs.repository.CourseExamMapper;
import com.mindskip.xzs.service.ExamPaperService;
import com.mindskip.xzs.service.QuestionService;

import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.service.enums.ActionEnum;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.utility.ModelMapperSingle;
import com.mindskip.xzs.utility.ExamUtil;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperTitleItemVM;
import com.mindskip.xzs.viewmodel.admin.question.QuestionEditRequestVM;
import com.mindskip.xzs.viewmodel.student.dashboard.PaperFilter;
import com.mindskip.xzs.viewmodel.student.dashboard.PaperInfo;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.domain.Question;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.CourseExam;
import com.mindskip.xzs.service.CourseExamService;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.viewmodel.teacher.exam.CourseExamRequestVM;
import com.mindskip.xzs.viewmodel.teacher.exam.CourseExamResponseVM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class ExamPaperServiceImpl extends BaseServiceImpl<ExamPaper> implements ExamPaperService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final ExamPaperMapper examPaperMapper;
    private final QuestionMapper questionMapper;
    private final TextContentService textContentService;
    private final QuestionService questionService;

    private final CourseExamService courseExamService;
    private final CourseStudentService courseStudentService;

    @Autowired
    public ExamPaperServiceImpl(ExamPaperMapper examPaperMapper, QuestionMapper questionMapper,
            TextContentService textContentService, QuestionService questionService,
            @Lazy CourseExamService courseExamService, CourseStudentService courseStudentService) {
        super(examPaperMapper);
        this.examPaperMapper = examPaperMapper;
        this.questionMapper = questionMapper;
        this.textContentService = textContentService;
        this.questionService = questionService;
        this.courseExamService = courseExamService;
        this.courseStudentService = courseStudentService;
    }

    @Override
    public PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> examPaperMapper.page(requestVM));
    }

    @Override
    public PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> examPaperMapper.taskExamPage(requestVM));
    }

    @Override
    public PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> examPaperMapper.studentPage(requestVM));
    }

    @Override
    @Transactional
    public ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, User user) {
        ActionEnum actionEnum = (examPaperEditRequestVM.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        Date now = new Date();
        List<ExamPaperTitleItemVM> titleItemsVM = examPaperEditRequestVM.getTitleItems();
        List<ExamPaperTitleItemObject> frameTextContentList = frameTextContentFromVM(titleItemsVM);
        String frameTextContentStr = JsonUtil.toJsonStr(frameTextContentList);

        ExamPaper examPaper;
        if (actionEnum == ActionEnum.ADD) {
            examPaper = modelMapper.map(examPaperEditRequestVM, ExamPaper.class);
            TextContent frameTextContent = new TextContent(frameTextContentStr, now);
            textContentService.insertByFilter(frameTextContent);
            examPaper.setFrameTextContentId(frameTextContent.getId());
            examPaper.setCreateTime(now);
            examPaper.setCreateUser(user.getId());
            examPaper.setDeleted(false);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaperMapper.insertSelective(examPaper);
        } else {
            examPaper = examPaperMapper.selectByPrimaryKey(examPaperEditRequestVM.getId());
            TextContent frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId());
            frameTextContent.setContent(frameTextContentStr);
            textContentService.updateByIdFilter(frameTextContent);
            modelMapper.map(examPaperEditRequestVM, examPaper);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaperMapper.updateByPrimaryKeySelective(examPaper);
        }
        return examPaper;
    }

    @Override
    public ExamPaperEditRequestVM examPaperToVM(Integer id) {
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(id);
        ExamPaperEditRequestVM vm = modelMapper.map(examPaper, ExamPaperEditRequestVM.class);
        vm.setLevel(examPaper.getGradeLevel());
        TextContent frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId());
        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JsonUtil
                .toJsonListObject(frameTextContent.getContent(), ExamPaperTitleItemObject.class);
        List<Integer> questionIds = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> q.getId()))
                .collect(Collectors.toList());
        List<Question> questions = questionMapper.selectByIds(questionIds);
        List<ExamPaperTitleItemVM> examPaperTitleItemVMS = examPaperTitleItemObjects.stream().map(t -> {
            ExamPaperTitleItemVM tTitleVM = modelMapper.map(t, ExamPaperTitleItemVM.class);
            List<QuestionEditRequestVM> questionItemsVM = t.getQuestionItems().stream().map(i -> {
                Question question = questions.stream().filter(q -> q.getId().equals(i.getId())).findFirst().get();
                QuestionEditRequestVM questionEditRequestVM = questionService.getQuestionEditRequestVM(question);
                questionEditRequestVM.setItemOrder(i.getItemOrder());
                return questionEditRequestVM;
            }).collect(Collectors.toList());
            tTitleVM.setQuestionItems(questionItemsVM);
            return tTitleVM;
        }).collect(Collectors.toList());
        vm.setTitleItems(examPaperTitleItemVMS);
        vm.setScore(ExamUtil.scoreToVM(examPaper.getScore()));
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateFormat(examPaper.getLimitStartTime()),
                    DateTimeUtil.dateFormat(examPaper.getLimitEndTime()));
            vm.setLimitDateTime(limitDateTime);
        }
        return vm;
    }

    @Override
    public List<PaperInfo> indexPaper(PaperFilter paperFilter) {
        return examPaperMapper.indexPaper(paperFilter);
    }

    @Override
    public Integer selectAllCount() {
        return examPaperMapper.selectAllCount();
    }

    @Override
    public List<Integer> selectMothCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mouthCount = examPaperMapper.selectCountByDate(startTime, endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }

    private void examPaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, ExamPaper examPaper,
            List<ExamPaperTitleItemVM> titleItemsVM) {
        Integer gradeLevel = examPaperEditRequestVM.getLevel(); // 直接从请求中获取level，不再依赖Subject
        Integer questionCount = titleItemsVM.stream()
                .mapToInt(t -> t.getQuestionItems().size()).sum();
        Integer score = titleItemsVM.stream().flatMapToInt(t -> t.getQuestionItems().stream()
                .mapToInt(q -> ExamUtil.scoreFromVM(q.getScore()))).sum();
        examPaper.setQuestionCount(questionCount);
        examPaper.setScore(score);
        examPaper.setGradeLevel(gradeLevel);
        List<String> dateTimes = examPaperEditRequestVM.getLimitDateTime();
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            examPaper.setLimitStartTime(DateTimeUtil.parse(dateTimes.get(0), DateTimeUtil.STANDER_FORMAT));
            examPaper.setLimitEndTime(DateTimeUtil.parse(dateTimes.get(1), DateTimeUtil.STANDER_FORMAT));
        }
    }

    private List<ExamPaperTitleItemObject> frameTextContentFromVM(List<ExamPaperTitleItemVM> titleItems) {
        AtomicInteger index = new AtomicInteger(1);
        return titleItems.stream().map(t -> {
            ExamPaperTitleItemObject titleItem = modelMapper.map(t, ExamPaperTitleItemObject.class);
            List<ExamPaperQuestionItemObject> questionItems = t.getQuestionItems().stream()
                    .map(q -> {
                        ExamPaperQuestionItemObject examPaperQuestionItemObject = modelMapper.map(q,
                                ExamPaperQuestionItemObject.class);
                        examPaperQuestionItemObject.setItemOrder(index.getAndIncrement());
                        return examPaperQuestionItemObject;
                    })
                    .collect(Collectors.toList());
            titleItem.setQuestionItems(questionItems);
            return titleItem;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CourseExamResponseVM> getCourseExams(Long courseId) {
        List<CourseExam> courseExams = courseExamService.findByCourseId(courseId);
        List<CourseExamResponseVM> responseList = new ArrayList<>();

        for (CourseExam exam : courseExams) {
            CourseExamResponseVM responseVM = modelMapper.map(exam, CourseExamResponseVM.class);
            ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamId());
            if (examPaper != null) {
                responseVM.setPaperName(examPaper.getName());
            }

            // 计算测验状态
            Date now = new Date();
            if (now.before(exam.getStartTime())) {
                responseVM.setStatus(0); // 未开始
            } else if (now.after(exam.getEndTime())) {
                responseVM.setStatus(2); // 已结束
            } else {
                responseVM.setStatus(1); // 进行中
            }

            // 获取学生人数
            Integer totalCount = courseStudentService.getStudentCountByCourseId(courseId);
            responseVM.setTotalCount(totalCount);

            // TODO: 获取已提交人数，这里暂时设为0，需要实现ExamPaperAnswerService扩展
            responseVM.setSubmitCount(0);

            responseList.add(responseVM);
        }

        return responseList;
    }

    @Override
    public CourseExamResponseVM getCourseExam(Long courseId, Integer examId) {
        CourseExam courseExam = courseExamService.findByCourseIdAndExamId(courseId, examId);
        if (courseExam == null) {
            return null;
        }

        CourseExamResponseVM responseVM = modelMapper.map(courseExam, CourseExamResponseVM.class);
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(courseExam.getExamId());
        if (examPaper != null) {
            responseVM.setPaperName(examPaper.getName());
        }

        // 计算测验状态
        Date now = new Date();
        if (now.before(courseExam.getStartTime())) {
            responseVM.setStatus(0); // 未开始
        } else if (now.after(courseExam.getEndTime())) {
            responseVM.setStatus(2); // 已结束
        } else {
            responseVM.setStatus(1); // 进行中
        }

        return responseVM;
    }

    @Override
    @Transactional
    public ExamPaper saveCourseExam(CourseExamRequestVM model, User user) {
        // 1. 检查试卷是否存在
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(model.getPaperId());
        if (examPaper == null) {
            throw new RuntimeException("试卷不存在");
        }

        // 2. 创建课程测验关联
        CourseExam courseExam = modelMapper.map(model, CourseExam.class);
        courseExam.setExamId(model.getPaperId());
        courseExam = courseExamService.create(courseExam, user);

        return examPaper;
    }

    @Override
    @Transactional
    public ExamPaper updateCourseExam(CourseExamRequestVM model, User user) {
        // 1. 检查试卷是否存在
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(model.getPaperId());
        if (examPaper == null) {
            throw new RuntimeException("试卷不存在");
        }

        // 2. 检查课程测验是否存在
        CourseExam courseExam = courseExamService.findByCourseIdAndExamId(model.getCourseId(), model.getId());
        if (courseExam == null) {
            throw new RuntimeException("测验不存在");
        }

        // 3. 更新课程测验关联
        courseExam = modelMapper.map(model, CourseExam.class);
        courseExam.setExamId(model.getPaperId());
        courseExam.setId(courseExam.getId()); // 保留原ID
        courseExamService.update(courseExam);

        return examPaper;
    }

    @Override
    @Transactional
    public void deleteCourseExam(Long courseId, Integer examId) {
        courseExamService.delete(courseId, examId);
    }

    @Override
    public List<ExamPaper> getAvailablePapers(Integer teacherId) {
        ExamPaperPageRequestVM requestVM = new ExamPaperPageRequestVM();
        requestVM.setCreateUser(teacherId);
        requestVM.setDeleted(false);
        return examPaperMapper.availablePapers(requestVM);
    }

    @Override
    public PageInfo<ExamPaper> teacherPage(ExamPaperPageRequestVM requestVM) {
        // 确保只查询未删除的试卷
        requestVM.setDeleted(false);
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> examPaperMapper.availablePapers(requestVM));
    }
}
