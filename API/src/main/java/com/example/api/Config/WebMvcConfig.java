package com.example.api.Config;

import com.example.api.Other.SessionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${upload.dir}")
    private String uploadDir;
    private final SessionInterceptor sessionInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns(
                        "/user/**",
                        "/expo/**",
                        "/booth/**",
                        "/content/**",
                        "/multiple/expo/**",
                        "/multiple/booth/**",
                        "/message/group/**",
                        "/message/ai/**"
                );
    }
}
