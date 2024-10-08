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

## F108 - Support Ticket Management (For customer role)
This is where the customer ask for suppoprt or whatever they need that involve the website or its service. The table which represent the format of a support tickets requires user to fill all information before it can be sent. Pressing the submit button will instantly send the ticket to the database where it will be stored, there is also response message displayed on the interface to inform if the process is completed. The user have options to edit or remove their submitted tickets, as long as there is no responses from the staff to that ticket. The backend of this feature will handle the CRUD operations using the input passed from the frontend.

## F109 - Response to Tickets (For staff/admin role)
This is the feature for staff user to respond to the submitted tickets. The interface will present a large table will all submiited tickets. Staff will press in every ticket in order to response to it as they are provided enough tools to handle it. To view all sent response, staff can press the button bellow the ticket table which will send them to other webpage, which present another table displaying all sent responses, edit and delete function is available. The backend of this feature will handle the CRUD operations using the input passed from the frontend.


# Ming Zhang - 24937524
## My Responsibilities
In this project, I am responsible for developing the Customer Shopping Cart and Checkout functionalities. These include backend logic and API endpoints for:

Use Case UC501 - Adding items to the cart, ensuring items are in stock, and updating the cart in real time.
Use Case UC502 - Handling secure checkout, processing payments, and generating order confirmations.
Use Case UC601 - Providing order access and filtering capabilities for admins to efficiently manage and search through orders.
Use Case UC602 - Enabling admins to update, change, or delete order statuses, keeping the order information accurate and up-to-date.
Functional and Non-Functional Requirements
Functional Requirements

## Shopping Cart Management (UC501):
Users can add items to their shopping cart, specify quantities, and see a running total of items in the cart.
If an item is out of stock, the system notifies the user and prevents adding it to the cart.
The cart persists across sessions, ensuring a smooth user experience even if the user logs out or closes the session.
Checkout Process (UC502):

Users can proceed to checkout, where the system calculates the total cost and interacts with a payment gateway to securely process the payment.
Upon successful payment, the system generates an order confirmation and updates the order status.
If payment fails, the system alerts the user/admin with options to retry the payment or cancel the order.
Order Management (UC601):

Admins can access and filter orders based on criteria like order date, customer ID, or order number.
The system displays the filtered order results, allowing admins to select a specific order for further review or modification.
Order Status Management (UC602):

Admins can update, change, or delete order statuses to reflect the current stage of each order.
Any status changes or deletions are logged for future reference, and notifications can be sent to the customer to keep them informed.
The system prevents invalid status changes and alerts the admin if a requested change is not permissible.
Non-Functional Requirements
Performance:

Cart updates should complete within 2 seconds to provide a seamless shopping experience for users.
The checkout process should take less than 3 seconds, including payment processing, to minimize potential frustration.
Security:

The checkout process must be compliant with PCI DSS standards, encrypting all sensitive payment data.
User sessions must be secure and persistent, ensuring that only authorized users can modify their shopping cart.
Admin access for order management is restricted and requires authentication.
Error Handling and Reliability:

The system should provide meaningful error messages for all actions. For instance, users receive notifications if items are out of stock or if payments fail.
During checkout, if payment fails due to connectivity issues, the system should allow the admin to retry the process or cancel the order.
Scalability:

The system should support concurrent user sessions without performance degradation.
Order management features should be able to handle a large volume of data, allowing admins to filter and manage orders efficiently.
