# Java based Cloud Platform
This cloud platform, written in Java using the Springboot framework, is designed to host apps

## Setup and Run
This application is designed to be deployed as 'ROOT' on a tomcat8 server.
* Make sure `application.properties` has details of your MySql instance in it. Replace the values as necessary. The file is currently setup to be: 
```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/example
spring.datasource.username=username
spring.datasource.password=example
```

* Compile using ```mvn install``` 
* Then put the compiled ROOT.war into your tomcat8 instance which will auto deploy it
