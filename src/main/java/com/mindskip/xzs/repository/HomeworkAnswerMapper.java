package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.HomeworkAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业答案Mapper接口
 */
@Mapper
public interface HomeworkAnswerMapper extends BaseMapper<HomeworkAnswer> {

    /**
     * 根据作业ID和学生ID查询所有答案
     */
    List<HomeworkAnswer> selectByHomeworkIdAndStudentId(Integer homeworkId, Integer studentId);

    /**
     * 检查学生是否已提交作业（至少有一个状态为已提交的答案）
     */
    HomeworkAnswer selectFirstSubmittedAnswerByHomeworkAndStudent(@Param("homeworkId") Integer homeworkId,
            @Param("studentId") Integer studentId);

    /**
     * 根据作业ID、题目ID和学生ID查询答案
     */
    HomeworkAnswer selectByHomeworkQuestionAndStudent(@Param("homeworkId") Integer homeworkId,
            @Param("questionId") Integer questionId,
            @Param("studentId") Integer studentId);

    /**
     * 根据作业ID查询所有学生答案
     */
    List<HomeworkAnswer> selectByHomeworkId(Integer homeworkId);

    /**
     * 根据学生ID查询答案列表
     */
    List<HomeworkAnswer> selectByStudentId(Integer studentId);
}