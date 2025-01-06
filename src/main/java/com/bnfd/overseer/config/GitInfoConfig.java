package com.bnfd.overseer.config;

import com.bnfd.overseer.model.api.info.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:git.properties")
public class GitInfoConfig {
    @Bean
    @ConfigurationProperties(prefix = "git")
    public GitInfo gitInfo() {
        return new GitInfo();
    }
}
