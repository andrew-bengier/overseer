package com.bnfd.overseer;

import com.bnfd.overseer.config.SystemTrayConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class OverseerApplication {

    private static ConfigurableApplicationContext context;

    @PostConstruct
    public void setToUtc() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        context = SpringApplication.run(OverseerApplication.class, args);
        SystemTrayConfig.setupTray();
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(OverseerApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}
