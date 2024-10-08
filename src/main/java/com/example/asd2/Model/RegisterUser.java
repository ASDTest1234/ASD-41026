package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class RegisterUser {

    @Id
    private String _id;
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
    private String unitNumber;

}
