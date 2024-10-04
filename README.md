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


# Hawken Su -24556197 

## F101 - User Login / Logout
The User Login and Logout utilises a Springboot Dependency called Spring Security, which acts as a massive filter that will intercept all connections from the users web-browser to the web application. Spring Security is a robust System that simplifies the process of implementing Security within any Java application. 
### Authentication
The authentication is done through SpringBoots its inbuilt Authentication Manager and Authentication Provider 

### Authorisation 

### Confidentiality 
The passwords are hidden in the database with BCrypt, which springboot can decode with it's decoder, and can cross reference from the database to the information given by the user. 

## F104 - User Browsing
