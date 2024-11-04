package com.project.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Chấp nhận tất cả các đường dẫn
                .allowedOrigins("*") // Địa chỉ mà bạn muốn cho phép
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức cho phép
                .allowedHeaders("*");
    }
}
