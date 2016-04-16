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
	private String messageIDParam = "messageID";
	private String userIDParam = "user_id";
	private String userHandlerParam = "userHandler";
	private String likesParam = "likes";
	private String valueParam = "value";
	
	private String userHandler;
	private String userID;
	private int likesCount;
	private boolean isLike;
	
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
        /*String*/ userID = (String)requestDic.get(this.userIDParam);
        String likesInputValue = (String)requestDic.get(this.valueParam);
        
        // create Key based on messageID
        Key messageKey = KeyFactory.createKey(this.PostEntity, postID);
        
        // retrieve Post Entity; check if message exists
		try {
			Entity post = datastore.get(messageKey);
            /*int*/ likesCount = (int) post.getProperty(this.likesParam);
        	
            // update likesCount for specific post 
            /*boolean*/ isLike = checkInput(likesInputValue);
            updateLikes(likesCount,isLike);

            post.setProperty(this.likesParam, likesCount);
            datastore.put(post);
        } catch (EntityNotFoundException e1) {
            this.writeErrorResponse(response, ": message does not exist");
        } catch (IllegalArgumentException e2) {
        	this.writeErrorResponse(response, ": invalid message key");
        } catch (DatastoreFailureException e3) {
        	this.writeErrorResponse(response, ": datastore error");
        } // end of check message exists
          
        /* write back success message */
        // get user name
        Key userKey = KeyFactory.createKey(UserEntity, userID);
            
        boolean fetchedUser = false;
		try {
			Entity userProfile = datastore.get(userKey);
			/*String*/ userHandler = (String)userProfile.getProperty(this.userHandlerParam);
			fetchedUser = true;
        } catch (EntityNotFoundException e1) {
            this.writeErrorResponse(response, ": fetching user");
        } catch (IllegalArgumentException e2) {
        	this.writeErrorResponse(response, ": invalid user key");
        } catch (DatastoreFailureException e3) {
        	this.writeErrorResponse(response, ": datastore error");
        } // end of check user retrieval 
		
        // display <user name/ID> (un)likes
		String user = userIdentifier (fetchedUser);
		displaySuccessMessage (response, user);
    }
    	
	public String userIdentifier (boolean b)
	{		
		if (b)
			return userHandler;
		else
			return userID;
	}
	
	public void displaySuccessMessage (HttpServletResponse resp, String usr)
	{
		if (isLike)
			this.writeSucessfulResponse(resp, usr + " likes!");
		else
			this.writeSucessfulResponse(resp, usr + " unlikes!");
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