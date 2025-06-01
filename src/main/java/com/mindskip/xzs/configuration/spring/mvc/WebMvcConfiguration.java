package com.mindskip.xzs.configuration.spring.mvc;

import com.mindskip.xzs.configuration.property.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @version 3.5.0
 * @description: The type Web mvc configuration.
 * Copyright (C), 2020-2025, 武汉思维跳跃科技有限公司
 * @date 2021/12/25 9:45
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfiguration.class);

    private final SystemConfig systemConfig;

    @Value("${file.resource-handler}")
    private String resourceHandler; // e.g., /resources/**

    @Value("${file.resource-locations}")
    private String resourceLocations; // e.g., file:./uploads/

    @Autowired
    public WebMvcConfiguration(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/student/index.html");
        registry.addRedirectViewController("/student", "/student/index.html");
        registry.addRedirectViewController("/admin", "/admin/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源 (css, js, images in classpath:/static/)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(31556926);

        // 上传的文件资源 (教学资源)
        logger.info("配置教学资源处理器: {} -> {}", resourceHandler, resourceLocations);
        registry.addResourceHandler(resourceHandler)
                .addResourceLocations(resourceLocations) // Ensure this is a valid Spring resource location (e.g., "file:./uploads/")
                .setCachePeriod(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 如果您有拦截器，可以在这里配置
        // List<String> securityIgnoreUrls = new java.util.ArrayList<>();
        // if (systemConfig.getWx() != null && systemConfig.getWx().getSecurityIgnoreUrls() != null) {
        //     securityIgnoreUrls = systemConfig.getWx().getSecurityIgnoreUrls();
        // }
        // String[] ignores = new String[securityIgnoreUrls.size()];
        // registry.addInterceptor(tokenHandlerInterceptor)
        //         .addPathPatterns("/api/wx/**")
        //         .excludePathPatterns(securityIgnoreUrls.toArray(ignores));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedOrigins("*") // 在生产环境中应配置为具体的域名
                .allowedHeaders("*");
    }
}
