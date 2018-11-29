package com.example.springdemo;

import com.example.springdemo.hystrix.MDCPropagatingHystrixCommandExecutionHook;
import com.example.springdemo.rx.LocalBean;
import com.example.springdemo.togglz.CustomEnumTogglzFeature;
import com.netflix.hystrix.strategy.HystrixPlugins;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.spi.FeatureProvider;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
@EnableHystrix
@EnableRetry
@EnableScheduling
public class SpringDemoApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringDemoApplication.class, args);
	}

	@Bean
	@Primary
	LocalBean localBean() {
		return new LocalBean("hello");
	}

	@Bean("ReqBean")
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	LocalBean requestBean() {
		return new LocalBean("request");
	}

	@Bean
	public FeatureProvider featureProvider() {
		return new EnumBasedFeatureProvider(CustomEnumTogglzFeature.class);
	}

	@PostConstruct
	public void setupHystrix() {
		HystrixPlugins.reset();
		HystrixPlugins.getInstance().registerCommandExecutionHook(new MDCPropagatingHystrixCommandExecutionHook());
	}

	/*@Bean
	public ServletRegistrationBean servletRegistrationBean(){
		return new ServletRegistrationBean(new TogglzConsoleServlet(),"/togglz/*");
	}*/

}


