package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.Assignment;
import com.mindskip.xzs.service.AssignmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业服务实现类
 * 注意：这是一个模拟实现，实际项目中需要添加数据库操作
 */
@Service
public class AssignmentServiceImpl implements AssignmentService {
    
    // 模拟数据存储，实际项目中应该使用数据库
    private static final List<Assignment> assignmentList = new ArrayList<>();
    private static Long idCounter = 1L;
    
    @Override
    public List<Assignment> getAssignmentsByCourseId(Long courseId) {
        // 模拟查询，实际项目中应该从数据库查询
        List<Assignment> result = new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            if (assignment.getCourseId().equals(courseId)) {
                result.add(assignment);
            }
        }
        return result;
    }
    
    @Override
    public Assignment getAssignmentById(Long id) {
        // 模拟查询，实际项目中应该从数据库查询
        for (Assignment assignment : assignmentList) {
            if (assignment.getId().equals(id)) {
                return assignment;
            }
        }
        return null;
    }
    
    @Override
    public Assignment createAssignment(Assignment assignment) {
        // 模拟创建，实际项目中应该保存到数据库
        assignment.setId(idCounter++);
        assignmentList.add(assignment);
        return assignment;
    }
    
    @Override
    public Assignment updateAssignment(Assignment assignment) {
        // 模拟更新，实际项目中应该更新数据库
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getId().equals(assignment.getId())) {
                assignmentList.set(i, assignment);
                return assignment;
            }
        }
        return null;
    }
    
    @Override
    public void deleteAssignment(Long id) {
        // 模拟删除，实际项目中应该从数据库删除
        assignmentList.removeIf(assignment -> assignment.getId().equals(id));
    }
} 