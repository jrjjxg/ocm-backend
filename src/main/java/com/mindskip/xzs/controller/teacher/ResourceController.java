package com.mindskip.xzs.controller.teacher;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Resource;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.service.FileUploadService;
import com.mindskip.xzs.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/teacher/courses")
public class ResourceController extends BaseApiController {

    private final FileUploadService fileUploadService;
    private final ResourceService resourceService;

    @Autowired
    public ResourceController(FileUploadService fileUploadService, ResourceService resourceService) {
        this.fileUploadService = fileUploadService;
        this.resourceService = resourceService;
    }

    /**
     * 获取课程下的所有资源
     * GET /api/teacher/courses/{courseId}/resources
     */
    @GetMapping("/{courseId}/resources")
    public RestResponse<List<Resource>> getResources(@PathVariable Long courseId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }
        // 这里可以添加权限校验，确保教师有权访问该课程的资源

        List<Resource> resources = resourceService.getResourcesByCourseId(courseId);
        return RestResponse.ok(resources);
    }

    /**
     * 获取特定资源详情
     * GET /api/teacher/courses/{courseId}/resources/{resourceId}
     */
    @GetMapping("/{courseId}/resources/{resourceId}")
    public RestResponse<Resource> getResource(@PathVariable Long courseId, @PathVariable Long resourceId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Resource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            return RestResponse.fail(404, "资源未找到");
        }

        // 校验资源是否属于指定课程
        if (!resource.getCourseId().equals(courseId)) {
            return RestResponse.fail(403, "资源不属于指定的课程");
        }

        return RestResponse.ok(resource);
    }

    /**
     * 创建新资源 (不含文件上传)
     * POST /api/teacher/courses/{courseId}/resources
     */
    @PostMapping("/{courseId}/resources")
    public RestResponse<Resource> createResource(@PathVariable Long courseId, @RequestBody Resource resource) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        resource.setCourseId(courseId);
        resource.setUploaderId(currentUser.getId());
        resource.setCreateTime(new Date());
        resource.setUpdateTime(new Date());

        Resource savedResource = resourceService.createResource(resource);
        return RestResponse.ok(savedResource);
    }

    /**
     * 上传资源文件
     * POST /api/teacher/courses/{courseId}/resources/upload
     */
    @PostMapping("/{courseId}/resources/upload")
    public RestResponse<Resource> uploadResource(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("type") Integer type) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        if (file.isEmpty()) {
            return RestResponse.fail(400, "上传文件不能为空");
        }

        try {
            String fileUrl = fileUploadService.storeFile(file, courseId);

            Resource resource = new Resource();
            resource.setTitle(title);
            resource.setDescription(description);
            resource.setType(type);
            resource.setUrl(fileUrl);
            resource.setFileSize(file.getSize());
            resource.setFileType(file.getContentType());
            resource.setCourseId(courseId);
            resource.setUploaderId(currentUser.getId());
            resource.setCreateTime(new Date());
            resource.setUpdateTime(new Date());

            Resource savedResource = resourceService.createResource(resource);
            return RestResponse.ok(savedResource);

        } catch (IOException e) {
            return RestResponse.fail(500, "文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            return RestResponse.fail(500, "处理请求失败: " + e.getMessage());
        }
    }

    /**
     * 更新资源信息
     * PUT /api/teacher/course/{courseId}/resource/{resourceId}
     */
    @PutMapping("/course/{courseId}/resource/{resourceId}")
    public RestResponse<Resource> updateResource(
            @PathVariable Long courseId,
            @PathVariable Long resourceId,
            @RequestBody Resource resourceDetails) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Resource existingResource = resourceService.getResourceById(resourceId);
        if (existingResource == null) {
            return RestResponse.fail(404, "资源未找到");
        }

        // 校验资源是否属于指定课程
        if (!existingResource.getCourseId().equals(courseId)) {
            return RestResponse.fail(403, "资源不属于指定的课程");
        }

        // 权限校验：确保是上传者本人才能修改
        if (!existingResource.getUploaderId().equals(currentUser.getId())) {
            return RestResponse.fail(403, "无权限修改此资源，只有上传者可以修改");
        }

        // 更新资源属性
        existingResource.setTitle(resourceDetails.getTitle());
        existingResource.setDescription(resourceDetails.getDescription());
        existingResource.setType(resourceDetails.getType());
        existingResource.setUpdateTime(new Date());

        Resource updatedResource = resourceService.updateResource(existingResource);
        if (updatedResource != null) {
            return RestResponse.ok(updatedResource);
        } else {
            return RestResponse.fail(500, "资源更新失败");
        }
    }

    /**
     * 删除资源
     * DELETE /api/teacher/course/{courseId}/resource/{resourceId}
     * 由于前端限制可能只能使用POST，这里仍保留POST方法
     */
    @DeleteMapping("/course/{courseId}/resource/{resourceId}")
    public RestResponse<?> deleteResource(@PathVariable Long courseId, @PathVariable Long resourceId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return RestResponse.fail(401, "用户未登录");
        }

        Resource existingResource = resourceService.getResourceById(resourceId);
        if (existingResource == null) {
            return RestResponse.fail(404, "资源未找到");
        }

        // 校验资源是否属于指定课程
        if (!existingResource.getCourseId().equals(courseId)) {
            return RestResponse.fail(403, "资源不属于指定的课程");
        }

        // 权限校验：确保是上传者本人才能删除
        if (!existingResource.getUploaderId().equals(currentUser.getId())) {
            return RestResponse.fail(403, "无权限删除此资源，只有上传者可以删除");
        }

        try {
            resourceService.deleteResourceById(resourceId, existingResource.getUrl());
            return RestResponse.ok("资源删除成功");
        } catch (Exception e) {
            return RestResponse.fail(500, "资源删除失败: " + e.getMessage());
        }
    }

    /**
     * 为兼容前端，保留POST方式的删除API
     * POST /api/teacher/course/{courseId}/resource/{resourceId}/delete
     */
    @PostMapping("/course/{courseId}/resource/{resourceId}/delete")
    public RestResponse<?> deleteResourceByPost(@PathVariable Long courseId, @PathVariable Long resourceId) {
        return deleteResource(courseId, resourceId);
    }
}