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
The authentication is done through SpringBoots its inbuilt Authentication Manager and Authentication Provider, Especially with the UserDetailService, by using this Spring Security will be able to cross reference from the Database and check if the user that is logging in can be able to access their specific URL's, else spring Security will capture any URL's that isn't configured, thus this allows for a sense of authentication through the checking of the user's roles. 

### Authorisation 
The authorisation of what user can access what is dependent upon the role that is given within the MongoDB database. The Roles within the database will need the "ROLE_" appended infront of the role you want to set due to Spring Securities way of managing roles within the System that need the "ROLE_" infront to recognise that it is a role. 

### Confidentiality 
The passwords are hidden in the database with BCrypt, which springboot can decode with it's encode by encoding the password that you give it when you are in the login and it compares the hashes to see if they are the same, and can cross reference from the database to the information given by the user. This information is retrieved by the User repository and the User Service is used to retrieve the Json Objects from the MongoDB database to Cross reference the information coming from the Login, this ensures that the users details are secured

## F104 - User Browsing
The user browsing is just what the user should be able to see, for example the customer should be able to access through the login and be able to view the products list and all the groceries that the store has, there will also be buttons that will redirect the user to specific locations such as the Cart, the Tickets, and logout. It uses the repository to call the data, where this data will be manipulated in the service layer to allow for CRUD operations, for my specific part I only needed the read part. Thymeleaf has great functions that allow for the integration of the data to be displayed in the frontend. 


# Ming Zhang - 24937524
In this project, I was responsible for the Customer Shopping Cart and Checkout functionalities. This includes managing the backend logic and API endpoints for adding items to the cart, managing the cart, and handling the checkout process.
Specifically, I am responsible for:

Use Case UC501 - Add Items to Cart: Implementing the functionality for users to add items to their cart and ensuring that the cart updates correctly.
Use Case UC502 - Checkout Selected Items: Implementing a secure and efficient checkout process, including payment processing and order confirmation.
Functional and Non-Functional Requirements
Functional Requirements
Shopping Cart Management:
Users can add items to their shopping cart (UC501).
The system allows users to specify item quantities and displays the total cost of items in the cart.
Users can view and update their cart, with real-time item availability and stock checks.
The cart should be persistent and accessible across sessions.
Checkout and Order Management:
Users can proceed to checkout, where the system calculates the total cost and processes payments securely (UC502).
The system generates an order confirmation and updates the order status upon successful payment.
Admins can access, filter, and manage orders, including updating or deleting order statuses (UC601, UC602).
Non-Functional Requirements
Performance:
Cart updates should be completed in under 2 seconds for a seamless shopping experience.
The checkout process should complete within 3 seconds to minimize user frustration.
Security:
The checkout process must comply with PCI DSS standards, ensuring that all sensitive payment information is encrypted.
The shopping cart must be secured against unauthorized modifications, and sessions must persist reliably.
Error Handling:
The system should handle errors gracefully, with user-friendly error messages. For instance, if an item is out of stock, the system notifies the user immediately and prevents the item from being added to the cart.
During checkout, if payment fails, the system provides options to retry or cancel the transaction, with appropriate notifications for each scenario.
Scalability:
The system should support multiple users interacting with their shopping carts and the checkout process simultaneously.
Admin functionalities, such as order filtering and updating order statuses, should be designed to handle a large volume of data efficiently.
