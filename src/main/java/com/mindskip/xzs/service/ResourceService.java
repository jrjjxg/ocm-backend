package com.mindskip.xzs.service;

import com.mindskip.xzs.domain.Resource;
import com.mindskip.xzs.repository.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ResourceService {
    private final ResourceMapper resourceMapper;
    private final FileUploadService fileUploadService;
    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    public ResourceService(ResourceMapper resourceMapper, FileUploadService fileUploadService) {
        this.resourceMapper = resourceMapper;
        this.fileUploadService = fileUploadService;
    }

    public Resource createResource(Resource resource) {
        resourceMapper.insertSelective(resource);
        return resource;
    }

    public List<Resource> getResourcesByCourseId(Long courseId) {
        return resourceMapper.selectByCourseId(courseId);
    }

    public Resource getResourceById(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public Resource updateResource(Resource resource) {
        resourceMapper.updateByPrimaryKeySelective(resource);
        return resourceMapper.selectByPrimaryKey(resource.getId());
    }

    @Transactional
    public void deleteResourceById(Long id, String fileUrl) throws IOException {
        resourceMapper.deleteByPrimaryKey(id);
        try {
            if (fileUrl != null && !fileUrl.isEmpty()) {
                fileUploadService.deleteFile(fileUrl);
            }
        } catch (IOException e) {
            logger.error("Error deleting physical file: " + fileUrl, e);
        }
    }
} 