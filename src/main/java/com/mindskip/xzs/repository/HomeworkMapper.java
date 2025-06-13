package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Homework;
import com.mindskip.xzs.viewmodel.admin.homework.HomeworkPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 作业Mapper接口
 */
@Mapper
public interface HomeworkMapper extends BaseMapper<Homework> {

    /**
     * 分页查询作业列表
     */
    List<Homework> page(HomeworkPageRequestVM requestVM);

    /**
     * 根据课程ID查询作业列表
     */
    List<Homework> selectByCourseId(Integer courseId);

    /**
     * 根据教师ID查询作业列表
     */
    List<Homework> selectByTeacherId(Integer teacherId);
}