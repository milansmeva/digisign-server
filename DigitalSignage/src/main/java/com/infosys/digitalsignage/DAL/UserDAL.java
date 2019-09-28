package com.infosys.digitalsignage.DAL;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;

import static com.mongodb.client.model.Filters.*;

import com.infosys.digitalsignage.Models.UserDetails;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserDAL {

	/*@Autowired
    @Qualifier("MongoClient")
	*/
	MongoClient mongoClient=new MongoClient("localhost",27017);
	
	public boolean AddUser(UserDetails user_det) {
		try {

			MongoDatabase database = mongoClient.getDatabase("signage");
			MongoCollection userdetails=database.getCollection("UserDetails");
			
			Document user = new Document("username", user_det.username)
			        .append("password",user_det.password);
			userdetails.insertOne(user);
		}
		catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	public boolean AuthenticateUser(UserDetails user_det) {
		
		try {
			MongoDatabase database = mongoClient.getDatabase("signage");
			MongoCollection userdetails=database.getCollection("UserDetails");
			
			Object BsonObject=userdetails.find(and(eq("username",user_det.username),eq("password",user_det.password))).first();
			if(BsonObject==null)
				return false;
		}
		catch(Exception ex) {
			return false;
		}
		
		return true;
	}
}
