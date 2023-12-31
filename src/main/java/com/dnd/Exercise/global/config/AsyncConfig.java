package com.dnd.Exercise.global.config;

import static com.dnd.Exercise.global.common.Constants.ASYNC_NOTIFICATION;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = ASYNC_NOTIFICATION)
    public Executor threadPoolExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors count {}", processors);

        executor.setThreadNamePrefix(ASYNC_NOTIFICATION);
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors*2);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(60);
        executor.initialize();
        return executor;
    }
}