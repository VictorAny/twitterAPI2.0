package com.example.myproject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
// Base class which holds helper methods to handle stuff we'll be doing often.
public class TwitterAPI2Servlet extends HttpServlet {
	
	TwitterAPI2Servlet(){
		super();
	}
	
	
	/*
	 * Return Successful response for a request. 
	 * args: Accepts an object. 
	 */
	public void writeSucessfulResponse(HttpServletResponse response, String returnData){
		try{
			this.writeResponse(response, "Sucess", returnData);
		}catch (IOException e){
			//Dunno..figure out later.
		}
	}
	
	/*
	 * Writes an unsucessful reponse for a request
	 * args: String representing error message
	 */
	public  void writeErrorResponse(HttpServletResponse response, String errorString){
		try{
			this.writeResponse(response, "Error", errorString);
		}catch (IOException e){
			// Dunno..do something.
		}
	}
	
	/*
	 * Handles writting JSON back to client
	 * args: reponse, returnType (sucess, error), and returnData 
	 */
	private void writeResponse(HttpServletResponse response, String returnType, Object returnData) throws IOException{
		Map<String, Object> myHash = new HashMap<String,Object>();
		myHash.put("response", returnType);
		myHash.put("data", returnData);
		Gson gson = new Gson();
		//gson.toJson() converts the object to json and returns a string.
		String returnObj = gson.toJson(myHash);
		 //Then we just write said string. 
		response.getWriter().println(returnObj);
	}
	
	/*
	 * Wrapper for writing non-json data to client
	 */
	public void write(HttpServletResponse response, String dataToWrite) throws IOException{
		response.getWriter().println(dataToWrite);
	}
	/*
	 * Serializes body of request and returns HashMap for handling and parsing
	 */
	public HashMap<String, Object> getRequestBodyMap(HttpServletRequest request) throws IOException{
		Gson gson = new Gson();
		String stringBody = getBody(request);
		Type jsonType = new TypeToken<HashMap<String, Object>>(){}.getType();
		HashMap<String, Object> myMap = gson.fromJson(stringBody, jsonType);
		return myMap;
	}
	
	/*
	 * Reads body of request into a string.
	 */
	private String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

}
