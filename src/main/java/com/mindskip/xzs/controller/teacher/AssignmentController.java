package com.mindskip.xzs.controller.teacher;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Assignment;
import com.mindskip.xzs.domain.AssignmentSubmit;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.AssignmentService;
import com.mindskip.xzs.service.AssignmentSubmitService;
import com.mindskip.xzs.service.TeacherCourseService;
import com.mindskip.xzs.viewmodel.admin.assignment.AssignmentSubmitVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/teacher/assignment")
public class AssignmentController extends BaseApiController {

    private final AssignmentService assignmentService;
    private final AssignmentSubmitService assignmentSubmitService;
    private final TeacherCourseService teacherCourseService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService, 
                               AssignmentSubmitService assignmentSubmitService,
                               TeacherCourseService teacherCourseService) {
        this.assignmentService = assignmentService;
        this.assignmentSubmitService = assignmentSubmitService;
        this.teacherCourseService = teacherCourseService;
    }

    /**
     * 获取课程的所有作业
     *
     * @param courseId 课程ID
     * @return 作业列表
     */
    @GetMapping("/course/{courseId}")
    public RestResponse<List<Assignment>> getAssignmentsByCourse(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), courseId)) {
            return RestResponse.fail(403, "没有权限");
        }

        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        return RestResponse.ok(assignments);
    }

    /**
     * 创建新作业
     *
     * @param assignment 作业信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public RestResponse<Assignment> createAssignment(@RequestBody Assignment assignment) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), assignment.getCourseId())) {
            return RestResponse.fail(403, "没有权限创建该课程的作业");
        }

        // 设置创建者ID和创建时间
        assignment.setCreatorId(currentUser.getId());
        assignment.setCreateTime(new Date());
        assignment.setUpdateTime(new Date());
        
        // 根据开始和结束时间设置状态
        Date now = new Date();
        if (now.before(assignment.getStartTime())) {
            assignment.setStatus(1); // 未开始
        } else if (now.after(assignment.getEndTime())) {
            assignment.setStatus(3); // 已结束
        } else {
            assignment.setStatus(2); // 进行中
        }

        try {
            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            return RestResponse.ok(createdAssignment);
        } catch (Exception e) {
            return RestResponse.fail(500, "创建作业失败: " + e.getMessage());
        }
    }

    /**
     * 更新作业
     *
     * @param id 作业ID
     * @param assignment 作业更新信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public RestResponse<Assignment> updateAssignment(@PathVariable Long id, @RequestBody Assignment assignment) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Assignment existingAssignment = assignmentService.getAssignmentById(id);
        if (existingAssignment == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), existingAssignment.getCourseId())) {
            return RestResponse.fail(403, "没有权限修改该作业");
        }

        // 只允许作业创建者修改
        if (!existingAssignment.getCreatorId().equals(currentUser.getId())) {
            return RestResponse.fail(403, "只有作业创建者可以修改");
        }

        // 更新字段
        existingAssignment.setTitle(assignment.getTitle());
        existingAssignment.setDescription(assignment.getDescription());
        existingAssignment.setStartTime(assignment.getStartTime());
        existingAssignment.setEndTime(assignment.getEndTime());
        existingAssignment.setTotalScore(assignment.getTotalScore());
        existingAssignment.setUpdateTime(new Date());
        
        // 更新附件URL（如果有）
        if (assignment.getAttachmentUrl() != null && !assignment.getAttachmentUrl().isEmpty()) {
            existingAssignment.setAttachmentUrl(assignment.getAttachmentUrl());
        }

        // 根据开始和结束时间更新状态
        Date now = new Date();
        if (now.before(existingAssignment.getStartTime())) {
            existingAssignment.setStatus(1); // 未开始
        } else if (now.after(existingAssignment.getEndTime())) {
            existingAssignment.setStatus(3); // 已结束
        } else {
            existingAssignment.setStatus(2); // 进行中
        }

        try {
            Assignment updatedAssignment = assignmentService.updateAssignment(existingAssignment);
            return RestResponse.ok(updatedAssignment);
        } catch (Exception e) {
            return RestResponse.fail(500, "更新作业失败: " + e.getMessage());
        }
    }

    /**
     * 删除作业
     *
     * @param id 作业ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public RestResponse<?> deleteAssignment(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Assignment existingAssignment = assignmentService.getAssignmentById(id);
        if (existingAssignment == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), existingAssignment.getCourseId())) {
            return RestResponse.fail(403, "没有权限删除该作业");
        }

        // 只允许作业创建者删除
        if (!existingAssignment.getCreatorId().equals(currentUser.getId())) {
            return RestResponse.fail(403, "只有作业创建者可以删除");
        }

        try {
            assignmentService.deleteAssignment(id);
            return RestResponse.ok("作业删除成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "删除作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取作业提交列表
     *
     * @param assignmentId 作业ID
     * @return 提交列表
     */
    @GetMapping("/{assignmentId}/submissions")
    public RestResponse<List<AssignmentSubmitVM>> getAssignmentSubmissions(@PathVariable Long assignmentId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        if (assignment == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), assignment.getCourseId())) {
            return RestResponse.fail(403, "没有权限查看该作业的提交");
        }

        List<AssignmentSubmitVM> submissions = assignmentSubmitService.getSubmissionsByAssignmentId(assignmentId);
        return RestResponse.ok(submissions);
    }

    /**
     * 批改作业
     *
     * @param submissionId 提交ID
     * @param gradeData 批改信息
     * @return 批改结果
     */
    @PutMapping("/submission/{submissionId}/grade")
    public RestResponse<?> gradeSubmission(@PathVariable Long submissionId, @RequestBody AssignmentSubmit gradeData) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        AssignmentSubmit submission = assignmentSubmitService.getSubmissionById(submissionId);
        if (submission == null) {
            return RestResponse.fail(404, "提交记录不存在");
        }

        Assignment assignment = assignmentService.getAssignmentById(submission.getAssignmentId());
        if (assignment == null) {
            return RestResponse.fail(404, "作业不存在");
        }

        // 验证该课程是否属于当前教师
        if (!teacherCourseService.validateTeacherCourse(currentUser.getId(), assignment.getCourseId())) {
            return RestResponse.fail(403, "没有权限批改该作业");
        }

        // 更新批改信息
        submission.setScore(gradeData.getScore());
        submission.setComment(gradeData.getComment());
        submission.setStatus(2); // 已批改
        submission.setCheckTime(new Date());
        submission.setTeacherId(currentUser.getId());

        try {
            assignmentSubmitService.updateSubmission(submission);
            return RestResponse.ok("批改成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "批改失败: " + e.getMessage());
        }
    }
} 