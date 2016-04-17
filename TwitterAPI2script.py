import urllib2
import urllib
import sys
import json


# Put the port the server is running here.
url = "http://localhost:8080"

requestBody = {
"name" : "Victor Anyirah1",
"userHandler" : "Coldsoldier2",
"garbage" : 1232132,
"user_id" : "12321",
"postCount" : 0
}




def makeGetRequest(url):
    req = urllib2.Request(url)
    response = urllib2.urlopen(req)
    the_page = response.read()
    print the_page



def makePostRequest(url, requestBody):
    data = json.dumps(requestBody)
    req = urllib2.Request(url, data)
    response = urllib2.urlopen(req)
    the_page = response.read()
    print the_page

#makePostRequest(url + "/user", requestBody)
#makeGetRequest(url + "/user?4")

requestBody = {
    "user_id" : "4",
    "text"  : "Holla, I love you",
    "tag" : "Intros"
}

#makePostRequest(url + "/message", requestBody)
#makeGetRequest(url + "/message?searchById=6")

requestBody = {
    "message_id" : "7",
    "user_id"   : "4"
}
makePostRequest(url + "/likes?type=LIKE", requestBody)
