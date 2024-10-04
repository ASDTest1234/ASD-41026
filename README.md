# ASD-41026
online supermarket ordering app

This app is a full java application that uses the Springboot framework, Spring Security for its User authentication and a MongoDB Atlas Database to store information. The dependencies are within the packet, The structure of the application is broken down into:

- **Model** = is the attributes of the item you are calling from the Database
- **Controller** = Directs the User to which page 
- **Repository** = Grabs items from the Database using the attributes from the model 
- **Services** = Allows for CRUD 
- **Implementation** = Any classes that implement a interface/ basically inheritance


## Requirements 
- java 17 or higher
- Whitelisted IP for MongoDB Atlas
- Add an .env file and add the MongoDB URI to it and connect it to the application.properties(**this is to prevent our URI connections from being leaked in github**)


# Hawken Su -24556197 

## F101 - User Login / Logout
The User Login and Logout utilises a Springboot Dependency called Spring Security, which acts as a massive filter that will intercept all connections from the users web-browser to the web application. Spring Security is a robust System that simplifies the process of implementing Security within any Java application. 
### Authentication
The authentication is done through SpringBoots its inbuilt Authentication Manager and Authentication Provider, Especially with the UserDetailService

### Authorisation 
The authorisation of what user can access what is dependent upon the role that is given within the MongoDB database. The Roles within the database will need the "ROLE_" appended infront of the role you want to set due to Spring Securities way of managing roles within the System that need the "ROLE_" infront to recognise that it is a role. 

### Confidentiality 
The passwords are hidden in the database with BCrypt, which springboot can decode with it's decoder, and can cross reference from the database to the information given by the user. This information is retrieved by the User repository and the User Service is used to retrieve the Json Objects from the MongoDB database to 

## F104 - User Browsing


