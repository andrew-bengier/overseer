package com.bnfd.overseer.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.core.io.*;

@Configuration
@PropertySource("classpath:git.properties")
public class GitInfoConfig {
    @Bean
    public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
        propsConfig.setLocation(new ClassPathResource("git.properties"));
        propsConfig.setIgnoreResourceNotFound(true);
        propsConfig.setIgnoreUnresolvablePlaceholders(true);

        return propsConfig;
    }
}
