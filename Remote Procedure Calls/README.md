# Assignment 3: Remote Procedure Calls

## Answer:
1. Why did I suggest that you use more than one thread for the implementation of the Intermediate
task?</br>
Because IntermediateHost receives messages from both Client and Server so it needs 2 different listening port which is 2 threads</br>
2. Is it necessary to use synchronized in the intermediate task? Explain</br>
It doesn't need to use synchronized because we can implement first in first out and 2 shared resources which are server queue and client queue. When the Client sends the data to Host, the data will be added to the client queue, and Host will look whether server queue is empty so it can return the response back to Client when Client is sending a request response. If the server queue is empty, the Client will be waiting. For Server, the Server sends the request data to Host, Host will look whether client queue is empty so it can send the Client data to Server. Server receive data and send the response to Host which will add the response to server queue. Server will then request for the next data. If the client queue is empty, the Server will be waiting


## Examples:
Client sends ```01test.txt0octet0``` -> IntermediateHost receives it in 23 sends```WAIT_FOR_RESPONSE``` to Client to port 3000
</br>
Client sends ```REQUEST_RESPONSE``` -> IntermediateHost receives it in 23 sends```WAIT_FOR_RESPONSE``` to Client in port 3000
</br>
Server sends ```REQUEST_DATA``` -> IntermediateHost in port 24, Host sends ```01test.txt0octet0```  to Server in port 69
</br>
Server sends ```0301``` as response -> IntermediateHost in port 24, Host sends ```WAIT_FOR_REQUEST``` to the Server
</br>
Client sends ```REQUEST_RESPONSE``` -> IntermediateHost sends ```0301``` to Client in 3000
</br>
Client receives  ```0301``` and sends ```02test.txt0octet0``` as starting the new cycle
</br>
Everything else will make Server throw exception as invalid request and terminate. Client and IntermediateHost still run


## Set up instructions:
Java version 17 or above is required. Open 3 terminals in the src directory. Run the program in the following order: First terminal runs ```java IntermediateHost.java```, second terminal runs ```Client.java``` then last terminal run ```Server.java```

## Files in the assignment:
* Client.java
* IntermediateHost.java
* Server.java
* DesignDiagrams.pdf
* README.md

