package com.github.DNeberize.demo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DbConsoleConfig {

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        ServletRegistrationBean<JakartaWebServlet> registration = new ServletRegistrationBean<>(
                new JakartaWebServlet(), "/db-console/*");
        Map<String, String> initParameters = new LinkedHashMap<>();
        initParameters.put("webAllowOthers", "false");
        initParameters.put("trace", "false");
        registration.setInitParameters(initParameters);
        registration.setName("H2Console");
        return registration;
    }
}