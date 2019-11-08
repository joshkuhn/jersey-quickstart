#Overview

This project is a basic REST service to modify passwd files on disk.  I wrote it for fun in October of 2019 to brush up on Jersey/Jetty.  This document discusses how to run the code, and has details on the API endpoints I implemented.  

**The API design, source code and documentation contained in the archive are copyright (c) 2019 Joshua M. Kuhn. All rights reserved.**

###Prerequisites

Install Maven and the latest Java JDK.  For testing POST requests, you can use curl or Postman if you prefer a GUI tool.  


###Running the Service

Extract the submission archive.  Inside the archive run the following command to download required JARs and run the code in Jetty:

    $> mvn jetty:run

This command will build the servlet’s code from source, then start up a Jetty servlet container. To verify that the server has started successfully, open http://localhost/ping in a web browser.  If the servlet is running, the API returns a response with status 200, and the message “OK” will be shown.

Alternatively from the command line, run:

    $> curl localhost/ping

To run unit tests, invoke the maven test goal as follows:

    $> mvn test


#API Design

Four APIs have been implemented, though the requirements didn’t specify the structure or design in much detail.  This section of the doc describes the way that the required APIs were implemented and should be useful when attempting to test them.

###Get Users List

Endpoint
Request Type
Parameters
http://localhost/users
GET
No URL parameters required.

The users API fetches a list of users from the mock passwd file stored in the solution archive.  Specifically, the relative path used by default for this file is “./passwd”, relative to the location where the mvn command is run.

###Get User Details

GET http://localhost/users?username=jsmith&username=hrearden

The /users endpoint also provides for the listing of a user’s GECOS field.  This field contains information about the user, such as their physical address, phone number and email address.  

The API accepts multiple user names in a single request.  Submitting a request with multiple username parameters returns the details for each user which exists in the passwd file. 

###Create User

POST http://localhost/users/create
```
{ 
	"userName": "test", 
	"password": "x", 
	"userId": "1002", 
	"groupId": "1002", 
	"gecos": "foobar", 
	"homeDir": "/users/test", 
	"shell": "/bin/bash" 
}
```

The create endpoint allows API consumers to create a new user on the system by POSTing a JSON payload to the URI above.  When invoked, the service will validate the JSON payload against the POJO user model in the source code, then write the new user to disk.

###Delete User

POST http://localhost/users/delete
```
{ 
	"userName": "test"
}
```

The delete API endpoint provides for the removal of all users matching the userName parameter supplied in the payload.  If no user matches the supplied userName, the request is still considered successful and will return a 200 OK response.

###Ping

GET http://localhost/ping

The /ping endpoint is a convenience API provided to allow for the service’s health to be determined without engaging an API which might cause a customer impacting side-effect.  It returns a 200 OK response when the service is up and running.
