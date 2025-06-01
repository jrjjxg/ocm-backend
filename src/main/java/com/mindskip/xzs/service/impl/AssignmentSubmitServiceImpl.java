package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.AssignmentSubmit;
import com.mindskip.xzs.service.AssignmentSubmitService;
import com.mindskip.xzs.viewmodel.admin.assignment.AssignmentSubmitVM;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业提交服务实现类
 * 注意：这是一个模拟实现，实际项目中需要添加数据库操作
 */
@Service
public class AssignmentSubmitServiceImpl implements AssignmentSubmitService {
    
    // 模拟数据存储，实际项目中应该使用数据库
    private static final List<AssignmentSubmit> submitList = new ArrayList<>();
    private static Long idCounter = 1L;
    
    @Override
    public List<AssignmentSubmitVM> getSubmissionsByAssignmentId(Long assignmentId) {
        // 模拟查询，实际项目中应该从数据库查询
        List<AssignmentSubmitVM> result = new ArrayList<>();
        for (AssignmentSubmit submit : submitList) {
            if (submit.getAssignmentId().equals(assignmentId)) {
                AssignmentSubmitVM vm = new AssignmentSubmitVM();
                vm.setId(submit.getId());
                vm.setAssignmentId(submit.getAssignmentId());
                vm.setStudentId(submit.getStudentId());
                // 实际项目中需要从User表查询学生姓名等信息
                vm.setStudentName("学生" + submit.getStudentId());
                vm.setStudentRealName("学生" + submit.getStudentId());
                vm.setContent(submit.getContent());
                vm.setAttachmentUrl(submit.getAttachmentUrl());
                vm.setSubmitTime(submit.getSubmitTime());
                vm.setStatus(submit.getStatus());
                vm.setScore(submit.getScore());
                vm.setComment(submit.getComment());
                vm.setCheckTime(submit.getCheckTime());
                vm.setTeacherId(submit.getTeacherId());
                // 实际项目中需要从User表查询教师姓名
                vm.setTeacherName(submit.getTeacherId() != null ? "教师" + submit.getTeacherId() : null);
                result.add(vm);
            }
        }
        return result;
    }
    
    @Override
    public AssignmentSubmit getSubmissionById(Long id) {
        // 模拟查询，实际项目中应该从数据库查询
        for (AssignmentSubmit submit : submitList) {
            if (submit.getId().equals(id)) {
                return submit;
            }
        }
        return null;
    }
    
    @Override
    public AssignmentSubmit createSubmission(AssignmentSubmit assignmentSubmit) {
        // 模拟创建，实际项目中应该保存到数据库
        assignmentSubmit.setId(idCounter++);
        submitList.add(assignmentSubmit);
        return assignmentSubmit;
    }
    
    @Override
    public AssignmentSubmit updateSubmission(AssignmentSubmit assignmentSubmit) {
        // 模拟更新，实际项目中应该更新数据库
        for (int i = 0; i < submitList.size(); i++) {
            if (submitList.get(i).getId().equals(assignmentSubmit.getId())) {
                submitList.set(i, assignmentSubmit);
                return assignmentSubmit;
            }
        }
        return null;
    }
    
    @Override
    public void deleteSubmission(Long id) {
        // 模拟删除，实际项目中应该从数据库删除
        submitList.removeIf(submit -> submit.getId().equals(id));
    }
    
    @Override
    public AssignmentSubmit getSubmissionByAssignmentIdAndStudentId(Long assignmentId, Integer studentId) {
        // 模拟查询，实际项目中应该从数据库查询
        for (AssignmentSubmit submit : submitList) {
            if (submit.getAssignmentId().equals(assignmentId) && submit.getStudentId().equals(studentId)) {
                return submit;
            }
        }
        return null;
    }
} 