package com.mindskip.xzs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.Homework;
import com.mindskip.xzs.domain.HomeworkAnswer;
import com.mindskip.xzs.domain.HomeworkQuestion;
import com.mindskip.xzs.repository.HomeworkAnswerMapper;
import com.mindskip.xzs.repository.HomeworkMapper;
import com.mindskip.xzs.repository.HomeworkQuestionMapper;
import com.mindskip.xzs.service.CourseStudentService;
import com.mindskip.xzs.service.HomeworkService;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkCreateVM;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkPageRequestVM;
import com.mindskip.xzs.viewmodel.student.homework.HomeworkAnswerVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 作业服务实现类
 */
@Service
public class HomeworkServiceImpl extends BaseServiceImpl<Homework> implements HomeworkService {

    private final HomeworkMapper homeworkMapper;
    private final HomeworkQuestionMapper homeworkQuestionMapper;
    private final HomeworkAnswerMapper homeworkAnswerMapper;
    private final CourseStudentService courseStudentService;
    private final com.mindskip.xzs.service.TextContentService textContentService;

    @Autowired
    public HomeworkServiceImpl(HomeworkMapper homeworkMapper,
            HomeworkQuestionMapper homeworkQuestionMapper,
            HomeworkAnswerMapper homeworkAnswerMapper,
            CourseStudentService courseStudentService,
            com.mindskip.xzs.service.TextContentService textContentService) {
        super(homeworkMapper);
        this.homeworkMapper = homeworkMapper;
        this.homeworkQuestionMapper = homeworkQuestionMapper;
        this.homeworkAnswerMapper = homeworkAnswerMapper;
        this.courseStudentService = courseStudentService;
        this.textContentService = textContentService;
    }

    @Override
    public PageInfo<Homework> page(HomeworkPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc")
                .doSelectPageInfo(() -> homeworkMapper.page(requestVM));
    }

    @Override
    @Transactional
    public void createHomework(HomeworkCreateVM createVM) {
        Homework homework = new Homework();
        homework.setCourseId(createVM.getCourseId());
        homework.setTitle(createVM.getTitle());
        homework.setDescription(createVM.getDescription());
        homework.setStartTime(createVM.getStartTime());
        homework.setEndTime(createVM.getEndTime());
        homework.setTotalScore(createVM.getTotalScore());
        homework.setStatus(createVM.getStatus());
        homework.setCreateUser(createVM.getTeacherId());
        homework.setCreateTime(new Date());

        // 保存作业
        homeworkMapper.insertSelective(homework);

        // 保存作业题目
        if (createVM.getQuestions() != null && !createVM.getQuestions().isEmpty()) {
            for (HomeworkCreateVM.HomeworkQuestionVM questionVM : createVM.getQuestions()) {
                HomeworkQuestion question = new HomeworkQuestion();
                question.setHomeworkId(homework.getId());
                question.setQuestionId(questionVM.getQuestionId());
                question.setScore(questionVM.getScore());
                question.setCreateTime(new Date());
                homeworkQuestionMapper.insertSelective(question);
            }
        }
    }

    @Override
    public Homework getHomeworkWithQuestions(Integer homeworkId) {
        return homeworkMapper.selectByPrimaryKey(homeworkId);
    }

    @Override
    public List<HomeworkQuestion> getHomeworkQuestions(Integer homeworkId) {
        return homeworkQuestionMapper.selectByHomeworkId(homeworkId);
    }

    @Override
    @Transactional
    public void submitAnswer(HomeworkAnswerVM answerVM) {
        // 获取作业的题目列表
        List<HomeworkQuestion> homeworkQuestions = homeworkQuestionMapper.selectByHomeworkId(answerVM.getHomeworkId());

        // 解析前端发送的答案数据
        Map<String, Object> answers = answerVM.getAnswers();
        if (answers == null) {
            throw new RuntimeException("答案数据不能为空");
        }

        // 为每个题目处理答案
        for (HomeworkQuestion hq : homeworkQuestions) {
            Integer questionId = hq.getQuestionId();
            Object answerValue = answers.get(questionId.toString());

            // 查找或创建答案记录
            HomeworkAnswer answer = homeworkAnswerMapper.selectByHomeworkQuestionAndStudent(
                    answerVM.getHomeworkId(), questionId, answerVM.getStudentId());

            if (answer == null) {
                // 创建新的答案记录
                answer = new HomeworkAnswer();
                answer.setHomeworkId(answerVM.getHomeworkId());
                answer.setQuestionId(questionId);
                answer.setStudentId(answerVM.getStudentId());
                answer.setStatus(answerVM.getStatus());
                answer.setSubmitTime(new Date());

                // 设置答案内容
                if (answerValue != null) {
                    if (answerValue instanceof List) {
                        // 多选题或文件上传题
                        answer.setAnswer(String.join(",", (List<String>) answerValue));
                    } else {
                        // 单选题、判断题、填空题、简答题
                        answer.setAnswer(answerValue.toString());
                    }
                }

                homeworkAnswerMapper.insertSelective(answer);
            } else {
                // 更新现有答案记录
                answer.setStatus(answerVM.getStatus());
                answer.setSubmitTime(new Date());

                // 更新答案内容
                if (answerValue != null) {
                    if (answerValue instanceof List) {
                        // 多选题或文件上传题
                        answer.setAnswer(String.join(",", (List<String>) answerValue));
                    } else {
                        // 单选题、判断题、填空题、简答题
                        answer.setAnswer(answerValue.toString());
                    }
                } else {
                    answer.setAnswer(null);
                }

                homeworkAnswerMapper.updateByPrimaryKeySelective(answer);
            }
        }
    }

    @Override
    public void autoGrade(Integer homeworkId, Integer studentId) {
        // TODO: 实现自动评分逻辑
        // 这里需要根据题目类型进行自动评分
    }

    @Override
    @Transactional
    public void manualGrade(Integer answerId, Integer score, String feedback) {
        HomeworkAnswer answer = homeworkAnswerMapper.selectByPrimaryKey(answerId);
        if (answer != null) {
            answer.setScore(score);
            answer.setManualScore(score);
            answer.setGradedTime(new Date());
            answer.setStatus(3); // 3表示已评分

            // 处理反馈内容
            if (feedback != null && !feedback.trim().isEmpty()) {
                try {
                    // 创建文本内容记录存储反馈
                    com.mindskip.xzs.domain.TextContent textContent = new com.mindskip.xzs.domain.TextContent();
                    textContent.setContent(feedback.trim());
                    textContent.setCreateTime(new Date());
                    textContentService.insertByFilter(textContent);

                    // 设置反馈内容ID
                    answer.setFeedbackTextContentId(textContent.getId());
                } catch (Exception e) {
                    // 如果保存反馈失败，记录日志但不影响评分
                    System.err.println("保存反馈内容失败: " + e.getMessage());
                }
            }

            homeworkAnswerMapper.updateByPrimaryKeySelective(answer);
        }
    }

    @Override
    public List<HomeworkAnswer> getStudentAnswers(Integer homeworkId, Integer studentId) {
        return homeworkAnswerMapper.selectByHomeworkIdAndStudentId(homeworkId, studentId);
    }

    @Override
    public Object getHomeworkStatistics(Integer homeworkId) {
        Map<String, Object> statistics = new HashMap<>();

        // 获取作业信息
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        if (homework == null) {
            return statistics;
        }

        // 获取该课程的选课学生总数
        Integer totalStudents = courseStudentService.getStudentCountByCourseId(homework.getCourseId().longValue());

        // 获取已提交的学生答案（按学生分组）
        List<HomeworkAnswer> allAnswers = homeworkAnswerMapper.selectByHomeworkId(homeworkId);

        // 统计已提交的学生数（去重）
        long submittedStudentCount = allAnswers.stream()
                .filter(answer -> answer.getStatus() != null && answer.getStatus() >= 2)
                .map(HomeworkAnswer::getStudentId)
                .distinct()
                .count();

        Integer submittedCount = (int) submittedStudentCount;
        Integer pendingCount = totalStudents - submittedCount;

        // 计算学生总分（每个学生的所有题目分数之和）
        Map<Integer, Integer> studentTotalScores = new HashMap<>();
        for (HomeworkAnswer answer : allAnswers) {
            if (answer.getStatus() != null && answer.getStatus() >= 2 && answer.getScore() != null) {
                studentTotalScores.merge(answer.getStudentId(), answer.getScore(), Integer::sum);
            }
        }

        // 计算统计数据
        double averageScore = 0.0;
        Integer maxScore = 0;
        Integer minScore = Integer.MAX_VALUE;

        if (!studentTotalScores.isEmpty()) {
            int totalScore = studentTotalScores.values().stream().mapToInt(Integer::intValue).sum();
            averageScore = (double) totalScore / studentTotalScores.size();
            maxScore = studentTotalScores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            minScore = studentTotalScores.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        }

        if (minScore == Integer.MAX_VALUE) {
            minScore = 0;
        }

        statistics.put("totalStudents", totalStudents); // 课程选课学生总数
        statistics.put("submittedCount", submittedCount); // 已提交人数
        statistics.put("pendingCount", pendingCount); // 未提交人数
        statistics.put("averageScore", Math.round(averageScore * 100.0) / 100.0); // 平均分
        statistics.put("maxScore", maxScore); // 最高分
        statistics.put("minScore", minScore); // 最低分

        return statistics;
    }

    @Override
    public Object getDashboardStatistics(Integer teacherId) {
        // 获取教师的作业统计数据
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        // 获取教师的所有作业
        List<Homework> allHomework = homeworkMapper.selectByTeacherId(teacherId);
        int totalHomework = allHomework.size();

        // 统计各状态的作业数量
        long ongoingCount = allHomework.stream().filter(h -> h.getStatus() != null && h.getStatus() == 2).count();
        long completedCount = allHomework.stream().filter(h -> h.getStatus() != null && h.getStatus() == 3).count();

        // 统计待批改作业数量（简化实现，实际需要根据提交情况判断）
        int pendingGradeCount = 0;
        for (Homework homework : allHomework) {
            if (homework.getStatus() != null && homework.getStatus() == 2) {
                // 对于进行中的作业，检查是否有未批改的提交
                List<HomeworkAnswer> answers = homeworkAnswerMapper.selectByHomeworkId(homework.getId());
                for (HomeworkAnswer answer : answers) {
                    if (answer.getStatus() != null && answer.getStatus() == 2) { // 已提交但未批改
                        pendingGradeCount++;
                        break; // 只要有一个未批改就计入待批改列表
                    }
                }
            }
        }

        stats.put("totalHomework", totalHomework);
        stats.put("ongoingHomework", (int) ongoingCount);
        stats.put("pendingGrade", pendingGradeCount);
        stats.put("completedHomework", (int) completedCount);

        return stats;
    }

    @Override
    @Transactional
    public void publishHomework(Integer homeworkId) {
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        if (homework != null) {
            homework.setStatus(2); // 2表示已发布
            homeworkMapper.updateByPrimaryKeySelective(homework);
        }
    }

    @Override
    @Transactional
    public void endHomework(Integer homeworkId) {
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        if (homework != null) {
            homework.setStatus(3); // 3表示已结束
            homeworkMapper.updateByPrimaryKeySelective(homework);
        }
    }

    @Override
    @Transactional
    public void updateHomework(HomeworkCreateVM updateVM) {
        Homework homework = homeworkMapper.selectByPrimaryKey(updateVM.getId());
        if (homework == null) {
            throw new RuntimeException("作业不存在");
        }

        homework.setTitle(updateVM.getTitle());
        homework.setDescription(updateVM.getDescription());
        homework.setStartTime(updateVM.getStartTime());
        homework.setEndTime(updateVM.getEndTime());
        homework.setTotalScore(updateVM.getTotalScore());
        homework.setStatus(updateVM.getStatus());

        // 更新作业
        homeworkMapper.updateByPrimaryKeySelective(homework);

        // 删除原有题目
        homeworkQuestionMapper.deleteByHomeworkId(homework.getId());

        // 重新保存题目
        if (updateVM.getQuestions() != null && !updateVM.getQuestions().isEmpty()) {
            for (HomeworkCreateVM.HomeworkQuestionVM questionVM : updateVM.getQuestions()) {
                HomeworkQuestion question = new HomeworkQuestion();
                question.setHomeworkId(homework.getId());
                question.setQuestionId(questionVM.getQuestionId());
                question.setScore(questionVM.getScore());
                question.setCreateTime(new Date());
                homeworkQuestionMapper.insertSelective(question);
            }
        }
    }

    // 保留原有的方法用于向后兼容
    @Transactional
    public Homework update(HomeworkCreateVM updateVM) {
        Homework homework = homeworkMapper.selectByPrimaryKey(updateVM.getId());
        if (homework == null) {
            return null;
        }

        homework.setTitle(updateVM.getTitle());
        homework.setDescription(updateVM.getDescription());
        homework.setStartTime(updateVM.getStartTime());
        homework.setEndTime(updateVM.getEndTime());
        homework.setTotalScore(updateVM.getTotalScore());
        homework.setStatus(updateVM.getStatus());

        // 更新作业
        homeworkMapper.updateByPrimaryKeySelective(homework);

        // 删除原有题目
        homeworkQuestionMapper.deleteByHomeworkId(homework.getId());

        // 重新保存题目
        if (updateVM.getQuestions() != null && !updateVM.getQuestions().isEmpty()) {
            for (HomeworkCreateVM.HomeworkQuestionVM questionVM : updateVM.getQuestions()) {
                HomeworkQuestion question = new HomeworkQuestion();
                question.setHomeworkId(homework.getId());
                question.setQuestionId(questionVM.getQuestionId());
                question.setScore(questionVM.getScore());
                question.setCreateTime(new Date());
                homeworkQuestionMapper.insertSelective(question);
            }
        }

        return homework;
    }

    public List<Homework> getByCourseId(Integer courseId) {
        return homeworkMapper.selectByCourseId(courseId);
    }

    public List<Homework> getByTeacherId(Integer teacherId) {
        return homeworkMapper.selectByTeacherId(teacherId);
    }

    public List<HomeworkQuestion> getQuestionsByHomeworkId(Integer homeworkId) {
        return homeworkQuestionMapper.selectByHomeworkId(homeworkId);
    }

    public HomeworkAnswer getStudentAnswer(Integer homeworkId, Integer studentId) {
        // 首先查找是否有已提交的答案
        return homeworkAnswerMapper.selectFirstSubmittedAnswerByHomeworkAndStudent(homeworkId, studentId);
    }

    public List<HomeworkAnswer> getAnswersByHomeworkId(Integer homeworkId) {
        return homeworkAnswerMapper.selectByHomeworkId(homeworkId);
    }

    public List<HomeworkAnswer> getAnswersByStudentId(Integer studentId) {
        return homeworkAnswerMapper.selectByStudentId(studentId);
    }
}