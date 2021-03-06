# Java based Cloud Platform
This cloud platform, written in Java using the Springboot framework, is designed to host apps

# Setup and Run
The cloud-platform application is designed to be deployed as 'ROOT' on a tomcat8 server. However it is currently configured to use H2 rather than mysql.

* Compile using ```mvn install``` 
* Then put the compiled ROOT.war into your tomcat8 instance which will auto deploy it.

## App Requirements
Once the cloud platform is deployed you can deploy additional apps. For them to function perfectly they need to follow the requirements below. You have to have the app deployed as ROOT to get this to work.

* WAR no larger than 50MB
* Authentication API:
  
  To authenticate we are using JSON Web Tokens stored in a cookie as 'access_token', or as a bearer token. To validate that the token you have is valid all  you need to do is send it to our open API at 
  
  * GET: ```/users/verify_token?access_token=*access_token*```
  
  * Response ```{username="username", verified="boolean"}``` 
  * If the verified is true then you will have the username of the token that just got sent. 
  
 * Peanut Bank API: To indicate that your app is being used all you need to do is send a request to 
   * GET ```/peanut_bank/usage?app_name=*app_name*&access_token=*access_token*```
