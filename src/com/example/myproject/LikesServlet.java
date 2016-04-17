package com.example.myproject;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreFailureException;
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
	private String PostEntity = "Post";
	private String UserEntity = "User";
	private String messageIDParam = "message_id";
	private String userIDParam = "user_id";
	private String userHandlerParam = "userHandler";
	private String likesParam = "likes";
	
	
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
        String userID = (String)requestDic.get(this.userIDParam);
        boolean isLike;
        String likesInputValue = request.getParameter("type");
        System.out.println(likesInputValue.trim().equalsIgnoreCase("LIKE"));
        /*boolean*/ isLike = checkInput(likesInputValue);
        System.out.println("Is likes is " + isLike);
        if (likesInputValue == null || postID == null || userID == null){
        	this.writeErrorResponse(response, "No paramater passed in");
        	return;
        }
        // create Key based on messageID
        Key messageKey = KeyFactory.createKey(this.PostEntity, Integer.parseInt(postID));
        Key userLikingKey = KeyFactory.createKey(this.UserEntity, Integer.parseInt(userID));
        
        // retrieve Post Entity; check if message exists
		try {
			//The user_id of the user liking/unliking the message must be valid.
			Entity userLikingMessage = datastore.get(userLikingKey);
			
			if (userLikingMessage == null){
				this.writeErrorResponse(response, "User id was not valid");
				return;
			}
			Entity post = datastore.get(messageKey);
            int likesCount = ((Long)post.getProperty(this.likesParam)).intValue();
            // update likesCount for specific post 
            likesCount = updateLikes(likesCount, isLike);
            post.setProperty(this.likesParam, likesCount);
            datastore.put(post);
            
            // display <user name/ID> (un)likes;
            String userHandler = (String)userLikingMessage.getProperty(this.userHandlerParam);
    		displaySuccessMessage (response, userHandler, isLike);
    		
        } catch (EntityNotFoundException e1) {
            this.writeErrorResponse(response, ": message does not exist");
        } catch (IllegalArgumentException e2) {
        	this.writeErrorResponse(response, ": invalid message key");
        } catch (DatastoreFailureException e3) {
        	this.writeErrorResponse(response, ": datastore error");
        } 
          		

    }
    	
	
	public void displaySuccessMessage (HttpServletResponse resp, String usr, boolean isLike)
	{
		if (isLike)
			this.writeSucessfulResponse(resp, usr + " likes!");
		else
			this.writeSucessfulResponse(resp, usr + " unlikes!");
	}
	
    public boolean checkInput (String param)
    {
        if (param.trim().equalsIgnoreCase("LIKE"))
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
