package com.example.asd2.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "register_users") // Adjust the collection name as needed
public class RegisterUser {

    @Id
    private String _id; // Unique identifier

    private String fName;
    private String lName;
    private String dob;
    private String email;
    private String phone;
    private String password;
    private String state;
    private String city;
    private String suburb;
    private String postcode;
    private String streetName;
    private String streetNumber;
    private String unitNumber; // Optional field
}
