package com.example.asd2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;
    private List<String> orderList;
    private String user_Id;

}
