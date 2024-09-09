package com.example.asd2.Model;

import com.example.asd2.repository.UserRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Documented;

@Setter // lombok to reduce boilerplate code
@Getter
@Document(collection = "users") // specifying which collection to get from.
public class Users {

    @Id
    private String username;
    private String password;
    private String email;
    private String role;




}
