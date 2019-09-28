package com.infosys.digitalsignage;

import org.bson.Document;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.infosys.digitalsignage.Models.MediaPlayer;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("mediaplayer")
public class MediaPlayerManagement {
	
	MongoClient mongoClient=new MongoClient("localhost",27017);
	
	@RequestMapping(path="AddMediaPlayer",method=RequestMethod.POST)
	public boolean AddMediaPlayer(@RequestBody MediaPlayer Mediaplayer) {
		
		try {
			MongoDatabase database = mongoClient.getDatabase("signage");
			
			MongoCollection mediaplayer=database.getCollection("MediaPlayer");
			
			Document bson=new Document("id",Mediaplayer.id)
					.append("password", Mediaplayer.password);
			
			mediaplayer.insertOne(bson);
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	@RequestMapping(path="RegisterMediaPLayer",method=RequestMethod.POST)
	public boolean RegisterMediaPLayer(@RequestBody MediaPlayer Mediaplayer) {
		try {
			MongoDatabase database = mongoClient.getDatabase("signage");
			
			MongoCollection<Document> mediaplayer=database.getCollection("MediaPlayer");
			
			Document doc=mediaplayer.
			findOneAndUpdate(and(eq("id",Mediaplayer.id),eq("password",Mediaplayer.password)), 
					combine(set("username", Mediaplayer.username), set("geopoint.lat",Mediaplayer.geopoint.lat ),
							set("geopoint.lon",Mediaplayer.geopoint.lon ),set("groups",Mediaplayer.groups),set("name",Mediaplayer.name)));
			
			if(doc==null)
				return false;
			
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	@RequestMapping(path="GetallMediaPlayersForUsers",method=RequestMethod.GET)
	public List<MediaPlayer> GetallMediaPlayersForUsers(@RequestParam("username") String username){
		try {
			
			MongoDatabase database = mongoClient.getDatabase("signage");
			
			MongoCollection<Document> mediaplayer=database.getCollection("MediaPlayer");
			
			List<MediaPlayer> mp=new LinkedList<MediaPlayer>();
			
			MongoCursor<Document> cursor= mediaplayer.find(eq("username",username)).batchSize(100).iterator();
			
			while (cursor.hasNext()) {
				
				String jsonString=cursor.next().toJson();
				
				Gson g = new Gson();
				mp.add(g.fromJson(jsonString, MediaPlayer.class));
				
		    }
			return mp;
			
			
		}
		catch(Exception ex) {
			return null;
		}
		
	}
}
