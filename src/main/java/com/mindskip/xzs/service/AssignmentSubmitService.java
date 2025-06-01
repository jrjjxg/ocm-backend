package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.AssignmentSubmit;
import com.mindskip.xzs.viewmodel.admin.assignment.AssignmentSubmitVM;
import java.util.List;

/**
 * 作业提交服务接口
 */
public interface AssignmentSubmitService {
    
    /**
     * 根据作业ID获取提交列表
     *
     * @param assignmentId 作业ID
     * @return 提交列表
     */
    List<AssignmentSubmitVM> getSubmissionsByAssignmentId(Long assignmentId);
    
    /**
     * 根据ID获取提交记录
     *
     * @param id 提交记录ID
     * @return 提交记录
     */
    AssignmentSubmit getSubmissionById(Long id);
    
    /**
     * 创建提交记录
     *
     * @param assignmentSubmit 提交信息
     * @return 创建的提交记录
     */
    AssignmentSubmit createSubmission(AssignmentSubmit assignmentSubmit);
    
    /**
     * 更新提交记录
     *
     * @param assignmentSubmit 提交信息
     * @return 更新后的提交记录
     */
    AssignmentSubmit updateSubmission(AssignmentSubmit assignmentSubmit);
    
    /**
     * 删除提交记录
     *
     * @param id 提交记录ID
     */
    void deleteSubmission(Long id);
    
    /**
     * 根据作业ID和学生ID获取提交记录
     *
     * @param assignmentId 作业ID
     * @param studentId 学生ID
     * @return 提交记录
     */
    AssignmentSubmit getSubmissionByAssignmentIdAndStudentId(Long assignmentId, Integer studentId);
} 