package com.qtelecom.call_recorder.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("Call-Recorder")
                .packagesToScan("com.qtelecom.call_recorder.web.controllers.v1")
                .pathsToMatch("/**")
                .build();
    }
}
