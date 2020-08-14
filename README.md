# back-end-test-logging

## Requirements

For building and running the application you need:
- [Maven 3](https://maven.apache.org) 

## Running the application locally
Execute the following command from the project root
- MacOS/Linux:
```shell
./mvnw spring-boot:run
```
- Windows:
```shell
mvnw spring-boot:run
```

## API Documentation
Api documentation is done using Swagger and is available from
- [Swagger UI](http://localhost:8080/back-end-test-logging/swagger-ui.html)

## Cloud scalability
For scalability logs need to be written in a standard format to be able to be parsed by services like say logstash which can then be pipelined into say Elasticsearch, Splunk or similar services. Centralised logging sytems can then store and provide meaningful insights about the application.   

