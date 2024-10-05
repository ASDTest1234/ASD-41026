package com.example.asd2.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
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

    // 手动定义构造函数
    public Products(String product_Id, String productName, String productDescription, int productStock, BigDecimal productPrice, String productType, String adminID) {
        this.product_Id = product_Id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productStock = productStock;
        this.productPrice = productPrice;
        this.productType = productType;
        this.adminID = adminID;
    }
}
