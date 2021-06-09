package com.dev.prepaid.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dev.prepaid.pool.NosqlFactory;
import com.dev.prepaid.pool.NosqlPool;
import com.dev.prepaid.pool.NosqlProperties;
import com.dev.prepaid.util.NosqlUtil;
import com.dev.prepaid.repository.nosql.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(NosqlProperties.class)
public class NosqlConfig {

    // factory
    @Bean
    public NosqlFactory nosqlFactory(NosqlProperties properties) {
        return new NosqlFactory(properties);
    }

    // connection pool 
    @Bean
    public NosqlPool nosqlPool(NosqlFactory nosqlFactory) {
        return new NosqlPool(nosqlFactory);
    }

    // auxiliary class
    @Bean
    public NosqlUtil NosqlUtil(NosqlPool nosqlPool) {
        return new NosqlUtil(nosqlPool);
    }
    
    @Bean
    public PrepaidCxProvInvocationsRepository PrepaidCxProvInvocationsRepository(NosqlPool nosqlPool) {
        return new PrepaidCxProvInvocationsRepository(nosqlPool);
    }
}
