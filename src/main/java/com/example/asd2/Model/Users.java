package com.example.asd2.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Users {

    @Id
    private String id;
    private String userID;
    private String username;  // Map to the "username" field
    private String email;     // Map to the "email" field
    private String password;  // Map to the "password" field
    private String role;      // Map to the "role" field
}
