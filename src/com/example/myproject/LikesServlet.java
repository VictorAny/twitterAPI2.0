package com.example.myproject;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Servlet implementation class LikesServlet
 */
public class LikesServlet extends TwitterAPI2Servlet {
	private static final long serialVersionUID = 1L;
	private String EntityKind = "Post";
	private String messageIDParam = "messageID";
	private String userIDParam = "user_id";
	private String likesParam = "likes";
	private String valueParam = "value";
	
    public LikesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> requestDic = this.getRequestBodyMap(request);
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        // retrieve parameters
        String postID = (String)requestDic.get(this.messageIDParam);
        String user = (String)requestDic.get(this.userIDParam);
        String likesInputValue = (String)requestDic.get(this.valueParam);
        
        // create Key based on messageID and retrieve Post Entity
        long uniqueId = Long.parseLong(postID);
        Key messageKey = KeyFactory.createKey(this.EntityKind, uniqueId);
        Entity post;
            
        // check if message exists
        try {
            post = datastore.get(messageKey);
                
            // update likesCount for specific post 
            int likesCount = (int) post.getProperty(this.likesParam);
            boolean isLike = checkInput(likesInputValue);
            updateLikes(likesCount,isLike);

            post.setProperty(this.likesParam, likesCount);
            datastore.put(post);
            
            if (isLike)
            	this.writeSucessfulResponse(response, user + " likes!");
            else
            	this.writeSucessfulResponse(response, user + " unlikes!");
                
            } catch (EntityNotFoundException e) {
                this.writeErrorResponse(response, "Error, message does not exist");
            }
    }
    
    public boolean checkInput (String param)
    {
        if (param.equalsIgnoreCase("LIKE"))
            return true;
        else
            return false;
    }
    
    public int updateLikes (int likesCount, boolean isLike)
    {
    	if (isLike)
            likesCount += 1;
    	else
    	{
            if (likesCount > 1)
                likesCount -= 1;
            else
                likesCount = 0;
    	}
    	
    	return likesCount;
    }
}