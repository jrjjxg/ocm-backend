package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.DiscussionService;
import com.mindskip.xzs.service.DiscussionReplyService;
import com.mindskip.xzs.viewmodel.student.discussion.DiscussionRequestVM;
import com.mindskip.xzs.viewmodel.student.discussion.DiscussionReplyRequestVM;
import com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionResponseVM;
import com.mindskip.xzs.viewmodel.teacher.discussion.DiscussionReplyResponseVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("StudentDiscussionController")
@RequestMapping(value = "/api/student")
public class DiscussionController extends BaseApiController {

    private final DiscussionService discussionService;
    private final DiscussionReplyService discussionReplyService;

    @Autowired
    public DiscussionController(DiscussionService discussionService,
            DiscussionReplyService discussionReplyService) {
        this.discussionService = discussionService;
        this.discussionReplyService = discussionReplyService;
    }

    /**
     * 获取课程讨论列表 (RESTful风格)
     */
    @GetMapping("/courses/{courseId}/discussions")
    public RestResponse<Map<String, Object>> getDiscussions(@PathVariable Long courseId,
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        User currentUser = getCurrentUser();

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(courseId, currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        List<DiscussionResponseVM> discussions = discussionService.getDiscussionsByCourseId(courseId, pageIndex,
                pageSize);
        int totalCount = discussionService.getDiscussionCountByCourseId(courseId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", discussions);
        result.put("total", totalCount);
        result.put("pageIndex", pageIndex);
        result.put("pageSize", pageSize);

        return RestResponse.ok(result);
    }

    /**
     * 创建讨论主题 (RESTful风格)
     */
    @PostMapping("/courses/{courseId}/discussions")
    public RestResponse<Long> createDiscussion(@PathVariable Long courseId,
            @Valid @RequestBody DiscussionRequestVM requestVM) {
        User currentUser = getCurrentUser();

        // 设置课程ID
        requestVM.setCourseId(courseId);

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(courseId, currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        try {
            Long discussionId = discussionService.createDiscussion(requestVM, currentUser.getId(), 1);
            return RestResponse.ok(discussionId);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 获取讨论详情
     */
    @GetMapping("/detail/{id}")
    public RestResponse<DiscussionResponseVM> detail(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        DiscussionResponseVM discussion = discussionService.getDiscussionById(id);
        if (discussion == null) {
            return RestResponse.fail(404, "讨论不存在");
        }

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(discussion.getCourseId(), currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        return RestResponse.ok(discussion);
    }

    /**
     * 更新讨论（仅限讨论创建者）
     */
    @PutMapping("/update/{id}")
    public RestResponse<Boolean> update(@PathVariable Long id, @Valid @RequestBody DiscussionRequestVM requestVM) {
        User currentUser = getCurrentUser();

        boolean success = discussionService.updateDiscussion(id, requestVM, currentUser.getId());
        if (success) {
            return RestResponse.ok(true);
        } else {
            return RestResponse.fail(500, "更新失败，可能没有权限或讨论不存在");
        }
    }

    /**
     * 删除讨论（仅限讨论创建者）
     */
    @DeleteMapping("/delete/{id}")
    public RestResponse<Boolean> delete(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        boolean success = discussionService.deleteDiscussion(id, currentUser.getId());
        if (success) {
            return RestResponse.ok(true);
        } else {
            return RestResponse.fail(500, "删除失败，可能没有权限或讨论不存在");
        }
    }

    /**
     * 获取置顶讨论 (RESTful风格)
     */
    @GetMapping("/courses/{courseId}/discussions/top")
    public RestResponse<List<DiscussionResponseVM>> getTopDiscussionsRestful(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(courseId, currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        List<DiscussionResponseVM> discussions = discussionService.getTopDiscussions(courseId);
        return RestResponse.ok(discussions);
    }

    /**
     * 获取精华讨论 (RESTful风格)
     */
    @GetMapping("/courses/{courseId}/discussions/essence")
    public RestResponse<List<DiscussionResponseVM>> getEssenceDiscussionsRestful(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(courseId, currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        List<DiscussionResponseVM> discussions = discussionService.getEssenceDiscussions(courseId);
        return RestResponse.ok(discussions);
    }

    /**
     * 获取讨论回复列表
     */
    @GetMapping("/replies/{discussionId}")
    public RestResponse<Map<String, Object>> getReplies(@PathVariable Long discussionId,
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        User currentUser = getCurrentUser();

        // 先获取讨论详情验证权限
        DiscussionResponseVM discussion = discussionService.getDiscussionById(discussionId);
        if (discussion == null) {
            return RestResponse.fail(404, "讨论不存在");
        }

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(discussion.getCourseId(), currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        List<DiscussionReplyResponseVM> replies = discussionReplyService.getRepliesByDiscussionId(discussionId,
                pageIndex, pageSize);
        int totalCount = discussionReplyService.getReplyCountByDiscussionId(discussionId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", replies);
        result.put("total", totalCount);
        result.put("pageIndex", pageIndex);
        result.put("pageSize", pageSize);

        return RestResponse.ok(result);
    }

    /**
     * 创建回复
     */
    @PostMapping("/reply")
    public RestResponse<Long> createReply(@Valid @RequestBody DiscussionReplyRequestVM requestVM) {
        User currentUser = getCurrentUser();

        // 先获取讨论详情验证权限
        DiscussionResponseVM discussion = discussionService.getDiscussionById(requestVM.getDiscussionId());
        if (discussion == null) {
            return RestResponse.fail(404, "讨论不存在");
        }

        // 验证学生对课程的权限
        if (!discussionService.validateCourseAccess(discussion.getCourseId(), currentUser.getId(), 1)) {
            return RestResponse.fail(403, "没有权限访问该课程");
        }

        try {
            Long replyId = discussionReplyService.createReply(requestVM, currentUser.getId(), 1);
            return RestResponse.ok(replyId);
        } catch (Exception e) {
            return RestResponse.fail(500, e.getMessage());
        }
    }

    /**
     * 删除回复（仅限回复创建者）
     */
    @DeleteMapping("/reply/{id}")
    public RestResponse<Boolean> deleteReply(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        boolean success = discussionReplyService.deleteReply(id, currentUser.getId());
        if (success) {
            return RestResponse.ok(true);
        } else {
            return RestResponse.fail(500, "删除失败，可能没有权限或回复不存在");
        }
    }
}