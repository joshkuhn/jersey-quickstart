#Overview

This project is a basic REST service to implement product-search functionality from files on disk.  I wrote it for fun in November of 2019 to brush up on Jersey/Jetty.  This document discusses how to run the code, and has details on the API endpoints I implemented.  

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



###Ping

GET http://localhost/ping

The /ping endpoint is a convenience API provided to allow for the service’s health to be determined without engaging an API which might cause a customer impacting side-effect.  It returns a 200 OK response when the service is up and running.
