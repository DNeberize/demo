package com.github.DNeberize.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/contact").setViewName("contact");
        registry.addViewController("/posts").setViewName("posts");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/about").setViewName("about");
    }
    
}
