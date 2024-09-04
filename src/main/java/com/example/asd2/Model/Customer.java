package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "users")
public class Customer {
    @Id
    private String id;
    private String username;
    private String password;
    private String role;
}
