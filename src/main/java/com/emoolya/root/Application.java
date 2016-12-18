package com.emoolya.root;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@ComponentScan(basePackages = { "com.emoolya.routes", "com.emoolya.service", "com.emoolya.bean" },
        excludeFilters = {@ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION)})

public class Application  {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/service/*");
        registration.setName("CamelServlet");
        return registration;
    }
}