package com.example.idempotency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
@ComponentScan(basePackages = "com.example.idempotency")
public class IdempotencyApplication {

	public IdempotencyApplication(){
		System.out.println("its scanning");
	}
	@Bean
	public IdempotencyFilter idempotencyFilter(IdempotencyService idempotencyService) {
		return new IdempotencyFilter(idempotencyService);
	}

	@Bean
	@ConditionalOnMissingBean
	public FilterRegistrationBean<IdempotencyFilter> myFilter(IdempotencyFilter idempotencyFilter){
		FilterRegistrationBean<IdempotencyFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(idempotencyFilter);
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		return registration;
	}

}
