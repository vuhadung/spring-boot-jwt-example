package com.fortna.hackathon;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class SimpleWebApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleWebApplication.class);

    @Bean(name = "asyncExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        logger.info("ThreadPoolTaskExecutor set");
        return executor;
    }
    
	public static void main(String[] args) {
		SpringApplication.run(SimpleWebApplication.class, args);
	}

}
