package com.example.asd2.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Product")
public class Products {

    @Id
    private String product_Id;
    private String productName;
    private String productDescription;
    private int productStock;
    private BigDecimal productPrice;
    private String productType;
    private String adminID;
}
