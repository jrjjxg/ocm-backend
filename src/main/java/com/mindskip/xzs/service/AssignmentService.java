package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.Assignment;
import java.util.List;

/**
 * 作业服务接口
 */
public interface AssignmentService {
    
    /**
     * 根据课程ID获取作业列表
     *
     * @param courseId 课程ID
     * @return 作业列表
     */
    List<Assignment> getAssignmentsByCourseId(Long courseId);
    
    /**
     * 根据ID获取作业
     *
     * @param id 作业ID
     * @return 作业
     */
    Assignment getAssignmentById(Long id);
    
    /**
     * 创建作业
     *
     * @param assignment 作业信息
     * @return 创建的作业
     */
    Assignment createAssignment(Assignment assignment);
    
    /**
     * 更新作业
     *
     * @param assignment 作业信息
     * @return 更新后的作业
     */
    Assignment updateAssignment(Assignment assignment);
    
    /**
     * 删除作业
     *
     * @param id 作业ID
     */
    void deleteAssignment(Long id);
} 