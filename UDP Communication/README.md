# Assignment 2: Introduction to UDP

## Overview:
Client.java will send the following requests ```01test.txt0octet0``` for reading request and ```02test.txt0octet0``` for writing request in bytes format to the IntermediateHost.java in port 23. Then it will wait for the respond on its receiving port 3000
</br>
IntermediateHost.java will receive a request from Client.java in port 23 and send the request to Server.java to port 69. Then it will wait for the Server to response back in any binding port and then send the data back to the Client throught port 3000
</br>
Server.java will receive the data in its reading port 69 and parse the data. The valid format:
start with <em>0 1 or 0 2</em><em>some text</em>0<em>some text</em>0 then nothing else after that! If its a reading request it will response with exactly 4 bytes 0301, writing is 0400 
</br>
Examples:
Client sends ```01test.txt0octet0``` -> IntermediateHost sends it -> Server
</br>
Server parses as read request then response ```0301``` -> IntermediateHost -> Client
</br>
Client sends ```02test.txt0octet0``` -> IntermediateHost sends it -> Server
</br>
Server parses as write request then response ```0400``` -> IntermediateHost -> Client
</br>
Everything else will make Server throw exception as invalid request and terminate. Client and IntermediateHost still run

## Set up instructions:
Java version 17 or above is required. Open 3 terminals in the src directory. First terminal runs ```java Server.java```, second terminal runs ```IntermediateHost.java``` then last terminal run ```Client.java```

Example output:
Server.java
![image](https://user-images.githubusercontent.com/76576373/217307496-1700cd0c-e143-4930-948c-f6a6ab66ffac.png)

IntermediateHost.java
![image](https://user-images.githubusercontent.com/76576373/217307974-e956bba2-0961-4936-981f-636602d82236.png)

Client.java
![image](https://user-images.githubusercontent.com/76576373/217308091-6eb76a86-31a1-4dbc-a208-f2af14fccec4.png)


## Files in the assignment:
* Client.java
* IntermediateHost.java
* Server.java
* DesignDiagrams.pdf

