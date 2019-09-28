package com.infosys.mediaplayer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(value={"com.infosys.mediaplayer"})
public class BeanConfig {
	
	@Bean
	public BlockingQueue<ScreenSaver> ScreenQueue(){
		return new LinkedBlockingQueue();
	}
	
	 @Bean  
	 public TaskExecutor getTaskExecutor() {  
	   ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();  
	   threadPoolTaskExecutor.setCorePoolSize(1);  
	   threadPoolTaskExecutor.setMaxPoolSize(5);  
	   return threadPoolTaskExecutor;  
	 }  
}
