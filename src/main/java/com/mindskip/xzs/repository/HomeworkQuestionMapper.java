package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.HomeworkQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 作业题目Mapper接口
 */
@Mapper
public interface HomeworkQuestionMapper extends BaseMapper<HomeworkQuestion> {

    /**
     * 根据作业ID查询题目列表
     */
    List<HomeworkQuestion> selectByHomeworkId(Integer homeworkId);

    /**
     * 根据作业ID删除题目
     */
    int deleteByHomeworkId(Integer homeworkId);
}