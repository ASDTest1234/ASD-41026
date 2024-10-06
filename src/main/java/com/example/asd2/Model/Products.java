package com.example.asd2.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

// uses lombok to do the getters and setters
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Product") // specifying the
public class Products {

    //attributes from of a Product from the database
    @Id
    private String product_Id;
    private String productName;
    private String productDescription;
    private int productStock;
    private BigDecimal productPrice; // big Decimal because its to do with price and I think I've read using floats is a wrong choice here.
    private String productType;
    private String adminID;
}
