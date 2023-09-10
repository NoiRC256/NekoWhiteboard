## Building

Run `build.bat` to build the application.
This performs the following command:
```
mvn clean package
```

## Usage

### Starting the Server

#### Via .bat file
Run `server.bat` to start the server at `localhost:3003`.

#### Via command line
```
java -jar target/server-jar-with-dependencies.jar <port>
```
For example:
```
java -jar target/server-jar-with-dependencies.jar 3003
```

### Starting the Client

#### Via .bat file
Run `client.bat` to start the client at `localhost:3002`.

#### Via command line
```
java -jar target/client-jar-with-dependencies.jar <server address> <server port>
```
For example:

```
java -jar target/client-jar-with-dependencies.jar localhost 3002
```
A client launcer window will appear. The server address and server port fields in the launcher window will be automatically filled base on the command line arguments.