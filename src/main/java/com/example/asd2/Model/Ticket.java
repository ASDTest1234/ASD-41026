package com.example.asd2.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id; 
    private String customerId;
    private String issue;
    private String description;
    private String date;


}
