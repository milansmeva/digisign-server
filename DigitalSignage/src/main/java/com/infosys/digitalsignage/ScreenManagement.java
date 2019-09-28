package com.infosys.digitalsignage;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.infosys.digitalsignage.Models.MediaPlayer;
import com.infosys.digitalsignage.Models.MediaPlayerPoller;
import com.infosys.digitalsignage.Models.ScreenSaver;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;


@RestController
@RequestMapping("screenmanagement")
public class ScreenManagement {


	MongoClient mongoClient=new MongoClient("localhost",27017);
	/*
	@RequestMapping(path="SaveScreen",method=RequestMethod.POST)
	public boolean SaveScreen(@RequestBody ScreenSaver screen) {
		try {
			MongoDatabase database = mongoClient.getDatabase("signage");
			
			MongoCollection mediaplayer=database.getCollection("SavedScreens");
			
			Gson gson=new Gson();
			
			String JsonString=gson.toJson(screen);
			Document doc=Document.parse(JsonString);
			
			mediaplayer.insertOne(doc);
			return true;
		}
		catch(Exception ex) {
			return false;
		}
		
	}
	*/
	
	@RequestMapping(path="DeployScreen",method=RequestMethod.POST)
	public boolean DeployScreen(@RequestBody ScreenSaver screen) {
		try {
			MongoDatabase database = mongoClient.getDatabase("signage");
			
			MongoCollection mediaplayer=database.getCollection("DeployScreens");
			screen.TimeStamp=new Date();
			Gson gson=new Gson();
			
			String JsonString=gson.toJson(screen);
			Document doc=Document.parse(JsonString);
			
			mediaplayer.insertOne(doc);
			return true;
		}
		catch(Exception ex) {
			return false;
		}
	}
	
	
	@RequestMapping(path="GetScreenUpdates",method=RequestMethod.POST)
	public List<ScreenSaver> GetScreenUpdates(@RequestBody MediaPlayerPoller mpp) {
		try {
			MongoDatabase database = mongoClient.getDatabase("signage");
			
			MongoCollection<Document> deployScreens=database.getCollection("DeployScreens");
			MongoCollection<Document> mediaDetails=database.getCollection("MediaPlayer");
			
			Document doc=mediaDetails.find(and(eq("id",mpp.id),eq("password",mpp.password))).first();
			if(doc==null)
				return null;
			
			String jsonString=doc.toJson();
			
			Gson g = new Gson();
			MediaPlayer mp=g.fromJson(jsonString, MediaPlayer.class);
			
			MongoCursor<Document> cursor=deployScreens.find(
					and(gt("TimeStamp", mpp.Timestamp),or(in("groups",mp.groups),in("MediaPlayersId",mpp.id)))).iterator();
			List<ScreenSaver> screensavers=new LinkedList<ScreenSaver>();
			while (cursor.hasNext()) {
				
				jsonString=cursor.next().toJson();
				
				screensavers.add(g.fromJson(jsonString, ScreenSaver.class));
				
		    }

			return screensavers;
		}
		catch(Exception ex) {
			return null;
		}
	}
}
