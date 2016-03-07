import urllib2

# Put the port the server is running here.
url = "http://localhost:8080"

requestBody = {
"name" : "Victor Anyirah",
"userhandler" : "Coldsoldier",
"garbage" : 1232132,
"user_id" : "12321",
"postCount" : 0
}

data = urllib2.urlencode(requestBody)
request = urlib2.Request(url, data)
response = urllib2.urlopen(request)
print response.geturl()
print response.info()
the_page = response.read()
print the_page

