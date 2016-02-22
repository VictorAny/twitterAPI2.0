package com.example.myproject;
import java.util.*;

public class User {

	protected String name;
	protected long userId;
	protected String userHandler;
	protected int postCount;
	
	public User(String name, String userHandler, long userId){
		this.name = name;
		this.userHandler = userHandler;
		this.postCount = 0;
		this.userId = userId;
	}
	
	public HashMap<String, Object> getUserInformation(){
		HashMap<String,Object> returnDict = new HashMap<String, Object>();
		returnDict.put("name", this.name);
		returnDict.put("user_id", this.userId);
		returnDict.put("postCount", this.postCount);
		returnDict.put("userHandler", this.userHandler);
		return returnDict;
 	}
	
}
