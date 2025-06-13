package com.mindskip.xzs.controller.student;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Resource;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.ResourceService;
import com.mindskip.xzs.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 学生课程资源控制器 - 兼容前端API调用路径
 */
@RestController("StudentCourseResourceController")
@RequestMapping(value = "/api/student/courses")
public class StudentCourseResourceController extends BaseApiController {

    private final ResourceService resourceService;
    private final StudentCourseService studentCourseService;

    @Autowired
    public StudentCourseResourceController(ResourceService resourceService, StudentCourseService studentCourseService) {
        this.resourceService = resourceService;
        this.studentCourseService = studentCourseService;
    }

    /**
     * 获取课程资源列表
     * 前端调用路径：POST /api/student/courses/{courseId}/resources
     *
     * @param courseId 课程ID
     * @param params   查询参数
     * @return 资源列表
     */
    @RequestMapping(value = "/{courseId}/resources", method = RequestMethod.POST)
    public RestResponse<PageInfo<Resource>> getCourseResources(@PathVariable Long courseId,
            @RequestBody(required = false) Map<String, Object> params) {
        User currentUser = getCurrentUser();

        // 检查学生是否已选修该课程
        boolean isEnrolled = studentCourseService.isStudentEnrolledInCourse(currentUser.getId(), courseId);
        if (!isEnrolled) {
            return RestResponse.fail(403, "您未选修该课程，无法访问资源");
        }

        try {
            // 解析分页参数
            Integer pageIndex = params != null && params.containsKey("pageIndex") ? (Integer) params.get("pageIndex")
                    : 1;
            Integer pageSize = params != null && params.containsKey("pageSize") ? (Integer) params.get("pageSize") : 10;
            String keyword = params != null && params.containsKey("keyword") ? (String) params.get("keyword") : null;
            String category = params != null && params.containsKey("category") ? (String) params.get("category") : null;
            Boolean favorite = params != null && params.containsKey("favorite") ? (Boolean) params.get("favorite")
                    : null;

            // 获取资源列表（这里需要实现分页查询）
            List<Resource> resources = resourceService.getResourcesByCourseId(courseId);

            // 简单的分页处理（实际应该在Service层实现）
            PageInfo<Resource> pageInfo = new PageInfo<>();
            pageInfo.setList(resources);
            pageInfo.setTotal(resources.size());
            pageInfo.setPageNum(pageIndex);
            pageInfo.setPageSize(pageSize);
            pageInfo.setPages((int) Math.ceil((double) resources.size() / pageSize));

            // 添加前端需要的额外字段
            pageInfo.setNavigatePages(8);
            pageInfo.setNavigatepageNums(new int[] { 1 });

            return RestResponse.ok(pageInfo);
        } catch (Exception e) {
            return RestResponse.fail(500, "获取资源列表失败：" + e.getMessage());
        }
    }

    /**
     * 下载课程资源
     * 前端调用路径：POST /api/student/courses/{courseId}/resources/{resourceId}/download
     *
     * @param courseId   课程ID
     * @param resourceId 资源ID
     * @return 下载结果
     */
    @RequestMapping(value = "/{courseId}/resources/{resourceId}/download", method = RequestMethod.POST)
    public RestResponse<?> downloadResource(@PathVariable Long courseId, @PathVariable Long resourceId) {
        User currentUser = getCurrentUser();

        // 检查学生是否已选修该课程
        boolean isEnrolled = studentCourseService.isStudentEnrolledInCourse(currentUser.getId(), courseId);
        if (!isEnrolled) {
            return RestResponse.fail(403, "您未选修该课程，无法下载资源");
        }

        try {
            // 这里应该实现实际的下载逻辑
            // 暂时返回成功
            return RestResponse.ok("下载成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "下载失败：" + e.getMessage());
        }
    }

    /**
     * 预览课程资源
     * 前端调用路径：GET /api/student/courses/{courseId}/resources/{resourceId}/preview
     *
     * @param courseId   课程ID
     * @param resourceId 资源ID
     * @return 预览结果
     */
    @RequestMapping(value = "/{courseId}/resources/{resourceId}/preview", method = RequestMethod.GET)
    public RestResponse<?> previewResource(@PathVariable Long courseId, @PathVariable Long resourceId) {
        User currentUser = getCurrentUser();

        // 检查学生是否已选修该课程
        boolean isEnrolled = studentCourseService.isStudentEnrolledInCourse(currentUser.getId(), courseId);
        if (!isEnrolled) {
            return RestResponse.fail(403, "您未选修该课程，无法预览资源");
        }

        try {
            // 这里应该实现实际的预览逻辑
            // 暂时返回成功
            return RestResponse.ok("预览功能待实现");
        } catch (Exception e) {
            return RestResponse.fail(500, "预览失败：" + e.getMessage());
        }
    }

    /**
     * 切换资源收藏状态
     * 前端调用路径：POST /api/student/courses/{courseId}/resources/{resourceId}/favorite
     *
     * @param courseId   课程ID
     * @param resourceId 资源ID
     * @return 收藏结果
     */
    @RequestMapping(value = "/{courseId}/resources/{resourceId}/favorite", method = RequestMethod.POST)
    public RestResponse<?> toggleResourceFavorite(@PathVariable Long courseId, @PathVariable Long resourceId) {
        User currentUser = getCurrentUser();

        // 检查学生是否已选修该课程
        boolean isEnrolled = studentCourseService.isStudentEnrolledInCourse(currentUser.getId(), courseId);
        if (!isEnrolled) {
            return RestResponse.fail(403, "您未选修该课程，无法收藏资源");
        }

        try {
            // 这里应该实现实际的收藏逻辑
            // 暂时返回成功
            return RestResponse.ok("收藏状态切换成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "收藏操作失败：" + e.getMessage());
        }
    }

    /**
     * 获取学生收藏的资源
     * 前端调用路径：POST /api/student/courses/{courseId}/resources/favorites
     *
     * @param courseId 课程ID
     * @param params   查询参数
     * @return 收藏的资源列表
     */
    @RequestMapping(value = "/{courseId}/resources/favorites", method = RequestMethod.POST)
    public RestResponse<PageInfo<Resource>> getFavoriteResources(@PathVariable Long courseId,
            @RequestBody(required = false) Map<String, Object> params) {
        User currentUser = getCurrentUser();

        // 检查学生是否已选修该课程
        boolean isEnrolled = studentCourseService.isStudentEnrolledInCourse(currentUser.getId(), courseId);
        if (!isEnrolled) {
            return RestResponse.fail(403, "您未选修该课程，无法访问收藏");
        }

        try {
            // 这里应该实现获取收藏资源的逻辑
            // 暂时返回空列表
            PageInfo<Resource> pageInfo = new PageInfo<>();
            pageInfo.setList(new ArrayList<>());
            pageInfo.setTotal(0);
            pageInfo.setPageNum(1);
            pageInfo.setPageSize(10);
            pageInfo.setPages(0);

            return RestResponse.ok(pageInfo);
        } catch (Exception e) {
            return RestResponse.fail(500, "获取收藏列表失败：" + e.getMessage());
        }
    }
}
