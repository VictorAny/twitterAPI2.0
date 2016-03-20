<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.*" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query.Filter" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate" %>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilter" %>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilterOperator" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!	link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
  <title>Messages</title>
</head>
<body>

<a href="index.html">Return to Main Page</a>
<h1>List of Messages</h1>
<%
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query q = new Query("Post");

		//Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);
    
		for (Entity result : pq.asIterable()) {
			  String userName = (String) result.getProperty("user_id");
			  String msg = (String) result.getProperty("text");
			  ArrayList<String> tagList = (ArrayList<String>) result.getProperty("tags");
			  String likes = (String) result.getProperty("likes");

			  pageContext.setAttribute("uName", userName);
			  pageContext.setAttribute("msg",msg);
			  pageContext.setAttribute("tags",tagList);
			  pageContext.setAttribute("likes",likes);
			  
			  
//			  System.out.println("User: "+userName+"First: "+firstName+"Last: "+lastName+"PW: "+pw);
%>			  
			  <p><b>${fn:escapeXml(uName)}</b> wrote:</p>
			  <blockquote>${fn:escapeXml(msg)} 
			  </blockquote>
			  <p> Tags: ${fn:escapeXml(tags)} </p><br>
			  <p> Likes: ${fn:escapeXml(likes)} 
			  <button type=button onclick="alert('Temporarily Unavailable')">Like! </button></p>
<%
			}
%>
</body>
</html>