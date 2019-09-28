package com.infosys.mediaplayer;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class MediaPlayerApplication {

	@Autowired
	static BlockingQueue<ScreenSaver> ScreenQueue;
	
	@Autowired
	static TaskExecutor executor; 
	
	@Autowired
	static MediaPlayerConfig mediaconfig;
	
	public static void main(String[] args) {
		
		executor.execute(new Runnable() {  
			   @Override  
			   public void run() {  
			     // your background task here  
				   RestTemplate restTemplate = new RestTemplate();
				   String fooResourceUrl
				     = "http://192.168.43.31:8080/screenmanagement/GetScreenUpdates";
				   
				   String id=mediaconfig.id;
				   String password=mediaconfig.password;
				   Date timestamp=new Date(0);
				   
				   while(true) {
					   
					   
					   try {
						   HttpEntity<MediaPlayerPoller> request = new HttpEntity<>(new MediaPlayerPoller(id,password,timestamp));
						   
						   LinkedList<ScreenSaver> listsc=restTemplate.postForEntity(fooResourceUrl,request ,ArrayofScreenSavers.class).getBody().listsc;
						   
						   if(listsc!=null)
						   {
						   
						   ScreenQueue.addAll(listsc);
						   
						   listsc.sort(new ScreenComparer());
						   timestamp=listsc.peekLast().TimeStamp;
						   }
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   }
				   
			   }  
			 });
		
		SpringApplication.run(MediaPlayerApplication.class, args);
	}
	
	@RequestMapping(path="GetNextScreen",method=RequestMethod.GET)
	public ScreenSaver GetNextScreen() {
		ScreenSaver sc=ScreenQueue.poll();
		if(sc!=null)
			ScreenQueue.add(sc);
		return sc;
	}
	
	class ArrayofScreenSavers{
		LinkedList<ScreenSaver> listsc;
	}
	
	
}
class ScreenComparer implements Comparator<ScreenSaver>{
	@Override
	public int compare(ScreenSaver first, ScreenSaver second) {
	       return first.TimeStamp.compareTo(second.TimeStamp);
	    }
}
