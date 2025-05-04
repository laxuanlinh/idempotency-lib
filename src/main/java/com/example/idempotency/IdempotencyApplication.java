package com.example.idempotency;

import com.example.idempotency.repository.IdempotencyLockRepository;
import com.example.idempotency.repository.IdempotencyRecordRepository;
import com.example.idempotency.service.InTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@AutoConfiguration
@ComponentScan(basePackages = "com.example.idempotency")
@EnableJpaRepositories(basePackages = "com.example.idempotency.repository")
public class IdempotencyApplication {


}
