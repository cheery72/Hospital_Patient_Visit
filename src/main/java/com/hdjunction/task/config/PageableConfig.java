package com.hdjunction.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePageableResolver() {
        return pageableResolver -> {
            pageableResolver.setOneIndexedParameters(true);
        };
    }

}
