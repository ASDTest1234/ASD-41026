package com.example.asd2.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// uses lombok to do the getters and setters
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Users {

    //attributes from of a User from the database
    @Id
    private String id;
    private String userID;
    private String username;
    private String email;
    private String password;
    private String role;
}
