[![N|Solid](https://www.konux.com/wp-content/themes/konux/img/konux.svg)](https://konux.com)

# Back-End Developer Case Study
This application receives post requests and communicate via Sockets to the server, then save the event message on a CSV file.
Communication with the server is performed via __Protocol Buffer messages__, encoded according to the protobuf binary format.
When sending messages to the server, in bytes, encoded as a Protocol Buffers varint.

This application contains a Server (Go) - Client (Java) program, and use Google Protocol Buffer for as communication. 
A proto file is defined and shared between the server and the client. 

### Prerequisites:
- Install Java 8+ installed [here](https://www.oracle.com/java/technologies/javase-downloads.html)
- Install Maven 2+ installed [here](https://maven.apache.org/install.html)
- Install Go [here](https://golang.org/doc/install)
- Postman [here](https://www.getpostman.com/downloads/) or as plugin for [Firefox](https://addons.mozilla.org/fr/firefox/addon/restclient/)

### Implementation
##### In server side:
- Compile shared/same event proto file into Go by protoc
- Deserialize the bytes by proto.Unmarshal
- Save the event message data on a CSV file

##### In client side:
- Compile shared/same proto file into Java by protoc
- Serialize the event message into bytes
- Send the encoded message data via Sockets to the server

### Running
##### Run Go server
Before running this application, make sure:
- Google Protocol Buffer Go package is installed using the command 
```console
$ go get -u -v github.com/golang/protobuf/{proto,protoc-gen-go}
```
- protoc-gen-go will be installed inside $GOBIN or by default in $GOPATH/bin
- In the main.go file, you have the same IP (**ConnHost**) and PORT (**ConnPort**) values as the Java Socket Client

The command below will run the server (*Make sure to be on the Server repository*):
```
$ go run main.go
```
##### Run Java Client
Before running this application, make sure:
- You followed the [Run Go Server section](#run-go-server)
- The application.properties in resources folder contains the same IP (**client.host**) and PORT (**client.port**) values as the Go Socket Server
- To build the project, in the folder containing pom.xml, run the command:
    - ```$ mvn package``` This command will create a JAR in the target folder

The command below will run the client (*Make sure to be on the Client repository*):
```
$ java -jar target/konux-0.0.1-SNAPSHOT.jar
```

##### Send POST Call via Postman
- Use the API http://localhost:8080/api/event in the Postman endpoint bar. Make sure that POST is selected in the Method type drop down.
- Set the header application as : 
    - **Name:** Content-Type
    - **Attribute value:** application/json
- This endpoint expects a Json body which contains the details of the event. 
- Copy and Paste the following in the body tab of Postman.

```json
{
"timestamp":"1518609008",
"userId":"123456",
"event":"5 hours of downtime occurred due to the release of version 1.0.5 of the system"
}
```

- Press Send and then see the Response Status.

#### Please check on both terminals (Server and Client) how the event message is being sent and processed. 
#### The data sent via Postman will be appended to events.csv file on (Konux/events.csv)

### Logs
##### Server Log
[![Client](https://www.zouhairguijjane.com/server.jpg)](https://www.zouhairguijjane.com/server.jpg)

##### Client Log
[![Client](https://www.zouhairguijjane.com/client.jpg)](https://www.zouhairguijjane.com/client.jpg)

##### Postman Log
[![Client](https://www.zouhairguijjane.com/postman.jpg)](https://www.zouhairguijjane.com/postman.jpg)
