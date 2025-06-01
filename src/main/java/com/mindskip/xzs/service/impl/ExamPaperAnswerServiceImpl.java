package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.*;
import com.mindskip.xzs.domain.dto.CourseStudentDTO;
import com.mindskip.xzs.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.xzs.domain.enums.ExamPaperTypeEnum;
import com.mindskip.xzs.domain.enums.QuestionTypeEnum;
import com.mindskip.xzs.domain.exam.ExamPaperTitleItemObject;
import com.mindskip.xzs.domain.other.KeyValue;
import com.mindskip.xzs.domain.other.ExamPaperAnswerUpdate;
import com.mindskip.xzs.domain.task.TaskItemAnswerObject;
import com.mindskip.xzs.repository.*;
import com.mindskip.xzs.repository.ExamPaperAnswerMapper;
import com.mindskip.xzs.repository.ExamPaperMapper;
import com.mindskip.xzs.repository.QuestionMapper;
import com.mindskip.xzs.repository.TaskExamCustomerAnswerMapper;
import com.mindskip.xzs.service.ExamPaperAnswerService;
import com.mindskip.xzs.service.ExamPaperQuestionCustomerAnswerService;
import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.ExamUtil;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperSubmitVM;
import com.mindskip.xzs.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import com.mindskip.xzs.viewmodel.teacher.exam.ExamResultResponseVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class ExamPaperAnswerServiceImpl extends BaseServiceImpl<ExamPaperAnswer> implements ExamPaperAnswerService {

    private final ExamPaperAnswerMapper examPaperAnswerMapper;
    private final ExamPaperMapper examPaperMapper;
    private final TextContentService textContentService;
    private final QuestionMapper questionMapper;
    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final TaskExamCustomerAnswerMapper taskExamCustomerAnswerMapper;
    private final CourseExamMapper courseExamMapper;
    private final CourseStudentMapper courseStudentMapper;
    private final UserMapper userMapper;

    @Autowired
    public ExamPaperAnswerServiceImpl(ExamPaperAnswerMapper examPaperAnswerMapper, ExamPaperMapper examPaperMapper, TextContentService textContentService, QuestionMapper questionMapper, ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService, TaskExamCustomerAnswerMapper taskExamCustomerAnswerMapper, CourseExamMapper courseExamMapper, CourseStudentMapper courseStudentMapper, UserMapper userMapper) {
        super(examPaperAnswerMapper);
        this.examPaperAnswerMapper = examPaperAnswerMapper;
        this.examPaperMapper = examPaperMapper;
        this.textContentService = textContentService;
        this.questionMapper = questionMapper;
        this.examPaperQuestionCustomerAnswerService = examPaperQuestionCustomerAnswerService;
        this.taskExamCustomerAnswerMapper = taskExamCustomerAnswerMapper;
        this.courseExamMapper = courseExamMapper;
        this.courseStudentMapper = courseStudentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public PageInfo<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperAnswerMapper.studentPage(requestVM));
    }


    @Override
    public ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user) {
        ExamPaperAnswerInfo examPaperAnswerInfo = new ExamPaperAnswerInfo();
        Date now = new Date();
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(examPaperSubmitVM.getId());
        ExamPaperTypeEnum paperTypeEnum = ExamPaperTypeEnum.fromCode(examPaper.getPaperType());
        //任务试卷只能做一次
        if (paperTypeEnum == ExamPaperTypeEnum.Task) {
            ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.getByPidUid(examPaperSubmitVM.getId(), user.getId());
            if (null != examPaperAnswer)
                return null;
        }
        String frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId()).getContent();
        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JsonUtil.toJsonListObject(frameTextContent, ExamPaperTitleItemObject.class);
        List<Integer> questionIds = examPaperTitleItemObjects.stream().flatMap(t -> t.getQuestionItems().stream().map(q -> q.getId())).collect(Collectors.toList());
        List<Question> questions = questionMapper.selectByIds(questionIds);
        //将题目结构的转化为题目答案
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> {
                            Question question = questions.stream().filter(tq -> tq.getId().equals(q.getId())).findFirst().get();
                            ExamPaperSubmitItemVM customerQuestionAnswer = examPaperSubmitVM.getAnswerItems().stream()
                                    .filter(tq -> tq.getQuestionId().equals(q.getId()))
                                    .findFirst()
                                    .orElse(null);
                            return ExamPaperQuestionCustomerAnswerFromVM(question, customerQuestionAnswer, examPaper, q.getItemOrder(), user, now);
                        })
                ).collect(Collectors.toList());

        ExamPaperAnswer examPaperAnswer = ExamPaperAnswerFromVM(examPaperSubmitVM, examPaper, examPaperQuestionCustomerAnswers, user, now);
        examPaperAnswerInfo.setExamPaper(examPaper);
        examPaperAnswerInfo.setExamPaperAnswer(examPaperAnswer);
        examPaperAnswerInfo.setExamPaperQuestionCustomerAnswers(examPaperQuestionCustomerAnswers);
        return examPaperAnswerInfo;
    }

    @Override
    @Transactional
    public String judge(ExamPaperSubmitVM examPaperSubmitVM) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectByPrimaryKey(examPaperSubmitVM.getId());
        List<ExamPaperSubmitItemVM> judgeItems = examPaperSubmitVM.getAnswerItems().stream().filter(d -> d.getDoRight() == null).collect(Collectors.toList());
        List<ExamPaperAnswerUpdate> examPaperAnswerUpdates = new ArrayList<>(judgeItems.size());
        Integer customerScore = examPaperAnswer.getUserScore();
        Integer questionCorrect = examPaperAnswer.getQuestionCorrect();
        for (ExamPaperSubmitItemVM d : judgeItems) {
            ExamPaperAnswerUpdate examPaperAnswerUpdate = new ExamPaperAnswerUpdate();
            examPaperAnswerUpdate.setId(d.getId());
            examPaperAnswerUpdate.setCustomerScore(ExamUtil.scoreFromVM(d.getScore()));
            boolean doRight = examPaperAnswerUpdate.getCustomerScore().equals(ExamUtil.scoreFromVM(d.getQuestionScore()));
            examPaperAnswerUpdate.setDoRight(doRight);
            examPaperAnswerUpdates.add(examPaperAnswerUpdate);
            customerScore += examPaperAnswerUpdate.getCustomerScore();
            if (examPaperAnswerUpdate.getDoRight()) {
                ++questionCorrect;
            }
        }
        examPaperAnswer.setUserScore(customerScore);
        examPaperAnswer.setQuestionCorrect(questionCorrect);
        examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        examPaperAnswerMapper.updateByPrimaryKeySelective(examPaperAnswer);
        examPaperQuestionCustomerAnswerService.updateScore(examPaperAnswerUpdates);

        ExamPaperTypeEnum examPaperTypeEnum = ExamPaperTypeEnum.fromCode(examPaperAnswer.getPaperType());
        switch (examPaperTypeEnum) {
            case Task:
                //任务试卷批改完成后，需要更新任务的状态
                ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(examPaperAnswer.getExamPaperId());
                Integer taskId = examPaper.getTaskExamId();
                Integer userId = examPaperAnswer.getCreateUser();
                TaskExamCustomerAnswer taskExamCustomerAnswer = taskExamCustomerAnswerMapper.getByTUid(taskId, userId);
                TextContent textContent = textContentService.selectById(taskExamCustomerAnswer.getTextContentId());
                List<TaskItemAnswerObject> taskItemAnswerObjects = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemAnswerObject.class);
                taskItemAnswerObjects.stream()
                        .filter(d -> d.getExamPaperAnswerId().equals(examPaperAnswer.getId()))
                        .findFirst().ifPresent(taskItemAnswerObject -> taskItemAnswerObject.setStatus(examPaperAnswer.getStatus()));
                textContentService.jsonConvertUpdate(textContent, taskItemAnswerObjects, null);
                textContentService.updateByIdFilter(textContent);
                break;
            default:
                break;
        }
        return ExamUtil.scoreToVM(customerScore);
    }

    @Override
    public ExamPaperSubmitVM examPaperAnswerToVM(Integer id) {
        ExamPaperSubmitVM examPaperSubmitVM = new ExamPaperSubmitVM();
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectByPrimaryKey(id);
        examPaperSubmitVM.setId(examPaperAnswer.getId());
        examPaperSubmitVM.setDoTime(examPaperAnswer.getDoTime());
        examPaperSubmitVM.setScore(ExamUtil.scoreToVM(examPaperAnswer.getUserScore()));
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperQuestionCustomerAnswerService.selectListByPaperAnswerId(examPaperAnswer.getId());
        List<ExamPaperSubmitItemVM> examPaperSubmitItemVMS = examPaperQuestionCustomerAnswers.stream()
                .map(a -> examPaperQuestionCustomerAnswerService.examPaperQuestionCustomerAnswerToVM(a))
                .collect(Collectors.toList());
        examPaperSubmitVM.setAnswerItems(examPaperSubmitItemVMS);
        return examPaperSubmitVM;
    }

    @Override
    public Integer selectAllCount() {
        return examPaperAnswerMapper.selectAllCount();
    }

    @Override
    public List<Integer> selectMothCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mouthCount = examPaperAnswerMapper.selectCountByDate(startTime, endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }


    /**
     * 用户提交答案的转化存储对象
     *
     * @param question               question
     * @param customerQuestionAnswer customerQuestionAnswer
     * @param examPaper              examPaper
     * @param itemOrder              itemOrder
     * @param user                   user
     * @param now                    now
     * @return ExamPaperQuestionCustomerAnswer
     */
    private ExamPaperQuestionCustomerAnswer ExamPaperQuestionCustomerAnswerFromVM(Question question, ExamPaperSubmitItemVM customerQuestionAnswer, ExamPaper examPaper, Integer itemOrder, User user, Date now) {
        ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer = new ExamPaperQuestionCustomerAnswer();
        examPaperQuestionCustomerAnswer.setQuestionId(question.getId());
        examPaperQuestionCustomerAnswer.setExamPaperId(examPaper.getId());
        examPaperQuestionCustomerAnswer.setQuestionScore(question.getScore());
        examPaperQuestionCustomerAnswer.setSubjectId(examPaper.getSubjectId());
        examPaperQuestionCustomerAnswer.setItemOrder(itemOrder);
        examPaperQuestionCustomerAnswer.setCreateTime(now);
        examPaperQuestionCustomerAnswer.setCreateUser(user.getId());
        examPaperQuestionCustomerAnswer.setQuestionType(question.getQuestionType());
        examPaperQuestionCustomerAnswer.setQuestionTextContentId(question.getInfoTextContentId());
        if (null == customerQuestionAnswer) {
            examPaperQuestionCustomerAnswer.setCustomerScore(0);
        } else {
            setSpecialFromVM(examPaperQuestionCustomerAnswer, question, customerQuestionAnswer);
        }
        return examPaperQuestionCustomerAnswer;
    }

    /**
     * 判断提交答案是否正确，保留用户提交的答案
     *
     * @param examPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer
     * @param question                        question
     * @param customerQuestionAnswer          customerQuestionAnswer
     */
    private void setSpecialFromVM(ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer, Question question, ExamPaperSubmitItemVM customerQuestionAnswer) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(examPaperQuestionCustomerAnswer.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                examPaperQuestionCustomerAnswer.setAnswer(customerQuestionAnswer.getContent());
                examPaperQuestionCustomerAnswer.setDoRight(question.getCorrect().equals(customerQuestionAnswer.getContent()));
                examPaperQuestionCustomerAnswer.setCustomerScore(examPaperQuestionCustomerAnswer.getDoRight() ? question.getScore() : 0);
                break;
            case MultipleChoice:
                String customerAnswer = ExamUtil.contentToString(customerQuestionAnswer.getContentArray());
                examPaperQuestionCustomerAnswer.setAnswer(customerAnswer);
                examPaperQuestionCustomerAnswer.setDoRight(customerAnswer.equals(question.getCorrect()));
                examPaperQuestionCustomerAnswer.setCustomerScore(examPaperQuestionCustomerAnswer.getDoRight() ? question.getScore() : 0);
                break;
            case GapFilling:
                String correctAnswer = JsonUtil.toJsonStr(customerQuestionAnswer.getContentArray());
                examPaperQuestionCustomerAnswer.setAnswer(correctAnswer);
                examPaperQuestionCustomerAnswer.setCustomerScore(0);
                break;
            default:
                examPaperQuestionCustomerAnswer.setAnswer(customerQuestionAnswer.getContent());
                examPaperQuestionCustomerAnswer.setCustomerScore(0);
                break;
        }
    }

    private ExamPaperAnswer ExamPaperAnswerFromVM(ExamPaperSubmitVM examPaperSubmitVM, ExamPaper examPaper, List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers, User user, Date now) {
        Integer systemScore = examPaperQuestionCustomerAnswers.stream().mapToInt(a -> a.getCustomerScore()).sum();
        long questionCorrect = examPaperQuestionCustomerAnswers.stream().filter(a -> a.getCustomerScore().equals(a.getQuestionScore())).count();
        ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();
        examPaperAnswer.setPaperName(examPaper.getName());
        examPaperAnswer.setDoTime(examPaperSubmitVM.getDoTime());
        examPaperAnswer.setExamPaperId(examPaper.getId());
        examPaperAnswer.setCreateUser(user.getId());
        examPaperAnswer.setCreateTime(now);
        examPaperAnswer.setSubjectId(examPaper.getSubjectId());
        examPaperAnswer.setQuestionCount(examPaper.getQuestionCount());
        examPaperAnswer.setPaperScore(examPaper.getScore());
        examPaperAnswer.setPaperType(examPaper.getPaperType());
        examPaperAnswer.setSystemScore(systemScore);
        examPaperAnswer.setUserScore(systemScore);
        examPaperAnswer.setTaskExamId(examPaper.getTaskExamId());
        examPaperAnswer.setQuestionCorrect((int) questionCorrect);
        boolean needJudge = examPaperQuestionCustomerAnswers.stream().anyMatch(d -> QuestionTypeEnum.needSaveTextContent(d.getQuestionType()));
        if (needJudge) {
            examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.WaitJudge.getCode());
        } else {
            examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        }
        return examPaperAnswer;
    }


    @Override
    public PageInfo<ExamPaperAnswer> adminPage(com.mindskip.xzs.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperAnswerMapper.adminPage(requestVM));
    }

    @Override
    public List<ExamResultResponseVM> getExamResults(Long courseId, Integer examId) {
        // 查询该测验下的所有学生答卷
        List<ExamPaperAnswer> answers = examPaperAnswerMapper.selectByCourseIdAndExamId(courseId, examId);
        
        // 查询课程学生
        List<CourseStudentDTO> students = courseStudentMapper.selectByCourseId(courseId);
        Map<Integer, CourseStudentDTO> studentMap = new HashMap<>();
        for (CourseStudentDTO student : students) {
            studentMap.put(student.getUserId(), student);
        }
        
        // 转换为视图模型
        List<ExamResultResponseVM> resultList = new ArrayList<>();
        for (ExamPaperAnswer answer : answers) {
            ExamResultResponseVM vm = new ExamResultResponseVM();
            vm.setId(answer.getId());
            vm.setScore(answer.getUserScore());
            vm.setSubmitTime(answer.getCreateTime());
            vm.setAnswerCompleted(Integer.valueOf(ExamPaperAnswerStatusEnum.Complete.getCode()).equals(answer.getStatus()));
            
            CourseStudentDTO student = studentMap.get(answer.getCreateUser());
            if (student != null) {
                vm.setStudentId(student.getStudentId());
                vm.setStudentName(student.getRealName());
            } else {
                User user = userMapper.selectByPrimaryKey(answer.getCreateUser());
                if (user != null) {
                    vm.setStudentId(user.getUserUuid());
                    vm.setStudentName(user.getRealName());
                }
            }
            
            resultList.add(vm);
        }
        
        return resultList;
    }

    @Override
    public Object getExamStatistics(Long courseId, Integer examId) {
        // 查询测验相关统计数据
        
        // 1. 查询测验信息
        CourseExam exam = courseExamMapper.selectByPrimaryKey(examId);
        if (exam == null || !exam.getCourseId().equals(courseId)) {
            throw new RuntimeException("测验不存在");
        }
        
        // 2. 查询课程学生总数
        Integer totalStudents = courseStudentMapper.countByCourseId(courseId);
        
        // 3. 查询已交卷学生数
        Integer submittedCount = examPaperAnswerMapper.countByExamId(examId);
        
        // 4. 计算平均分、最高分等
        Integer totalScore = 0;
        Integer maxScore = 0;
        List<ExamPaperAnswer> answers = examPaperAnswerMapper.selectByCourseIdAndExamId(courseId, examId);
        
        // 分数区间统计
        Map<String, Integer> scoreDistribution = new HashMap<>();
        scoreDistribution.put("0-60", 0);
        scoreDistribution.put("60-70", 0);
        scoreDistribution.put("70-80", 0);
        scoreDistribution.put("80-90", 0);
        scoreDistribution.put("90-100", 0);
        
        for (ExamPaperAnswer answer : answers) {
            Integer score = answer.getUserScore();
            totalScore += score;
            if (score > maxScore) {
                maxScore = score;
            }
            
            // 分数区间统计
            if (score < 60) {
                scoreDistribution.put("0-60", scoreDistribution.get("0-60") + 1);
            } else if (score < 70) {
                scoreDistribution.put("60-70", scoreDistribution.get("60-70") + 1);
            } else if (score < 80) {
                scoreDistribution.put("70-80", scoreDistribution.get("70-80") + 1);
            } else if (score < 90) {
                scoreDistribution.put("80-90", scoreDistribution.get("80-90") + 1);
            } else {
                scoreDistribution.put("90-100", scoreDistribution.get("90-100") + 1);
            }
        }
        
        // 计算平均分
        double averageScore = submittedCount > 0 ? (double) totalScore / submittedCount : 0;
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalStudents);
        result.put("submitCount", submittedCount);
        result.put("averageScore", averageScore);
        result.put("maxScore", maxScore);
        result.put("scoreDistribution", scoreDistribution);
        
        return result;
    }

    @Override
    public Object getExamAnswers(Long courseId, Integer examId, Object query) {
        // 根据查询条件获取答卷列表
        
        // 转换查询条件
        Map<String, Object> queryMap = (Map<String, Object>) query;
        Integer pageIndex = queryMap.get("pageIndex") != null ? Integer.valueOf(queryMap.get("pageIndex").toString()) : 1;
        Integer pageSize = queryMap.get("pageSize") != null ? Integer.valueOf(queryMap.get("pageSize").toString()) : 10;
        String studentName = queryMap.get("studentName") != null ? queryMap.get("studentName").toString() : null;
        Integer scoreFrom = queryMap.get("scoreFrom") != null ? Integer.valueOf(queryMap.get("scoreFrom").toString()) : null;
        Integer scoreTo = queryMap.get("scoreTo") != null ? Integer.valueOf(queryMap.get("scoreTo").toString()) : null;
        
        // 分页查询
        PageHelper.startPage(pageIndex, pageSize);
        List<ExamPaperAnswer> answers = examPaperAnswerMapper.selectByExamIdAndConditions(examId, studentName, scoreFrom, scoreTo);
        
        // 转换为ViewModel
        List<Map<String, Object>> answerList = new ArrayList<>();
        for (ExamPaperAnswer answer : answers) {
            Map<String, Object> answerMap = new HashMap<>();
            answerMap.put("id", answer.getId());
            answerMap.put("studentId", "");
            answerMap.put("studentName", "");
            
            // 获取学生信息
            User user = userMapper.selectByPrimaryKey(answer.getCreateUser());
            if (user != null) {
                answerMap.put("studentName", user.getRealName());
                answerMap.put("studentId", user.getUserUuid());
            }
            
            answerMap.put("score", answer.getUserScore());
            answerMap.put("submitTime", DateTimeUtil.dateFormat(answer.getCreateTime()));
            answerMap.put("answerCompleted", Integer.valueOf(ExamPaperAnswerStatusEnum.Complete.getCode()).equals(answer.getStatus()));
            answerMap.put("status", answer.getStatus());
            
            answerList.add(answerMap);
        }
        
        // 构建分页结果
        PageInfo<ExamPaperAnswer> pageInfo = new PageInfo<>(answers);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", answerList);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());
        
        return result;
    }

    @Override
    public void evaluateAnswer(Long courseId, Integer examId, Integer answerId, Object data) {
        // 根据评阅数据更新答卷
        
        // 1. 获取答卷信息
        ExamPaperAnswer answer = examPaperAnswerMapper.selectByPrimaryKey(answerId);
        if (answer == null) {
            throw new RuntimeException("答卷不存在");
        }
        
        // 2. 获取评阅数据
        Map<String, Object> evaluationData = (Map<String, Object>) data;
        List<Map<String, Object>> questionEvaluations = (List<Map<String, Object>>) evaluationData.get("questionEvaluations");
        
        // 3. 更新题目得分
        int totalScore = 0;
        for (Map<String, Object> evaluation : questionEvaluations) {
            Integer questionId = Integer.valueOf(evaluation.get("questionId").toString());
            Integer score = Integer.valueOf(evaluation.get("score").toString());
            String comment = evaluation.get("comment") != null ? evaluation.get("comment").toString() : "";
            
            // 更新题目答案的得分
            ExamPaperQuestionCustomerAnswer questionAnswer = examPaperQuestionCustomerAnswerService.getByAnswerIdAndQuestionId(answerId, questionId);
            if (questionAnswer != null) {
                questionAnswer.setCustomerScore(score);
                examPaperQuestionCustomerAnswerService.updateByIdFilter(questionAnswer);
                
                // 可选：保存评语
                if (comment != null && !comment.isEmpty()) {
                    // 保存评语逻辑，可以存储在TextContent中
                }
                
                totalScore += score;
            }
        }
        
        // 4. 更新答卷总分
        answer.setUserScore(totalScore);
        answer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        updateByIdFilter(answer);
    }
}
