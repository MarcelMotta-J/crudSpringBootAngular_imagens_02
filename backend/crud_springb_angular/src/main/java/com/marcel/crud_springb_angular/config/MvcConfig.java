package com.marcel.crud_springb_angular.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure a resource handler for the URL path "/uploads/**"
        // and map it to the "file:uploads/" directory on the file system.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // The path is relative to the application's working directory
    }

}
