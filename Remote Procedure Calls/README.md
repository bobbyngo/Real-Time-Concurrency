# Assignment 3: Remote Procedure Calls

## Overview:
```Client.java``` will send the following requests ```01test.txt0octet0``` for reading request to IntermediateHost in port 23. Client will receive the response from the IntermediateHost in port 3000 to confirm it is received. Client then will send a request to IntermediateHost for the reponse from its data. After that the ```02test.txt0octet0``` writing request in bytes format will be sent to the IntermediateHost.java in port 23. The same workflow will happen
</br>
```IntermediateHost.java``` will have 2 threads
</br>
Thread 1 is listening to port 23: 
</br>
Receive datagram of Client in port 23 -> send message to Client through port 3000 to tell Host has received the message. Add the  data to the share queue and clear the queue. After that, if the Client sends something it tells Client to wait until its turn Thread 2 is listening to port 24: 
</br>
Receive request from Server in port 24 -> send the data to Server through port 69 if it's available in the queue, else tell the Server to wait. After receiving response of the data from server, add the response to the queue and clear it. If the Server sends something, tell the Server to wait 
</br>
```Server.java``` send the request packet to port 24 and receive the data in port 69. If the data is not received, Server will keep sending the request packet. Else it will parse the data and send the result to port 24 
</br>
Examples:
```
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
```

## Set up instructions:
Java version 17 or above is required. Open 3 terminals in the src directory. Run the program in the following order: First terminal runs ```java IntermediateHost.java```, second terminal runs ```Client.java``` then last terminal run ```Server.java```

## Files in the assignment:
* Client.java
* IntermediateHost.java
* Server.java
* DesignDiagrams.pdf
* README.md

