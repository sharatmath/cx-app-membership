package com.dev.prepaid;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.dev.prepaid.config.RejectedTaskHandler;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableRetry
@EnableJpaAuditing
public class PrepaidApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrepaidApplication.class, args);
	}
	
	@Bean 
	public InitData getInitData(){
		return new InitData();
	}
	
	@Bean(name = "CXInvocationExecutor")
    public Executor dmCpExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		RejectedTaskHandler handler=new RejectedTaskHandler(); 
//      executor.setRejectedExecutionHandler(handler);
        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(100);
//        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("InvocationThread-");
        executor.initialize();
        return executor;
    }
	
	@Bean(name = "tokenExecutor")
    public Executor tokenExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		RejectedTaskHandler handler=new RejectedTaskHandler(); 
      executor.setRejectedExecutionHandler(handler);
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.setThreadNamePrefix("TokenThread-");
        executor.initialize();
        return executor;
    }
	
	@Bean(name = "redemptionExecutor")
    public Executor redemptionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		RejectedTaskHandler handler=new RejectedTaskHandler(); 
//      executor.setRejectedExecutionHandler(handler);
        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(100);
//        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("RedemptionResponsysThread-");
        executor.initialize();
        return executor;
    }
}
