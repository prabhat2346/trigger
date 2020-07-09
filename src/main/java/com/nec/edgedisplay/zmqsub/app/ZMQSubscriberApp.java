
package com.nec.edgedisplay.zmqsub.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.nec.edgedisplay.*", "com.necti.httpclient.*" })
@EnableCaching
@EnableAsync

/**
 * 
 * @author Rahul Sharma
 *
 */
public class ZMQSubscriberApp {

	public static void main(String[] args) {
		SpringApplication.run(ZMQSubscriberApp.class, args);

	}

	@Bean
	public RestTemplate template(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();

	}

	@Bean("threadPoolTaskExecutor")
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		executor.setThreadNamePrefix("Async-");
		return executor;
	}

}
