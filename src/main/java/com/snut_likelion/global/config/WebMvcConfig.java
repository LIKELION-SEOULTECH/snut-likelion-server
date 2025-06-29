package com.snut_likeliion.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 요청을 uploadDir(= .../images) 폴더로 매핑
        String absolutePath = Paths.get(uploadDir)
                                   .toAbsolutePath()
                                   .toUri()
                                    .toString();

        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("file:" + absolutePath);
    }
}
