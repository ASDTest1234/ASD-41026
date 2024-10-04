package com.example.asd2.Service;

import com.example.asd2.Model.Products;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final MongoCollection<Document> productCollection;

    public ProductService(MongoDatabase database) {
        this.productCollection = database.getCollection("Product");
    }

    public Optional<Products> getProductByName(String productName) {
        logger.info("Fetching product by name: {}", productName);
        Document productDoc = productCollection.find(eq("productName", productName)).first();

        if (productDoc == null) {
            logger.warn("Product not found for name: {}", productName);
            return Optional.empty();
        }

        Products product = documentToProduct(productDoc);
        logger.debug("Retrieved product details: {}", product);
        return Optional.of(product);
    }

    private Products documentToProduct(Document doc) {
        logger.info("Converting document to product: {}", doc.toJson());
        String productId = "";

        Object idObj = doc.get("_id");
        if (idObj instanceof ObjectId) {
            productId = ((ObjectId) idObj).toHexString();
        } else if (idObj instanceof String) {
            productId = (String) idObj;
        }

        // 处理 productStock 字段，以防止类型转换错误
        int stock;
        Object stockObj = doc.get("productStock");
        if (stockObj instanceof Integer) {
            stock = (Integer) stockObj;
        } else if (stockObj instanceof Long) {
            stock = ((Long) stockObj).intValue();  // 将 Long 转换为 int
        } else {
            logger.warn("Unexpected type for productStock: {}", stockObj != null ? stockObj.getClass() : "null");
            stock = 0;  // 赋予默认值
        }

        // 处理 productPrice 字段
        BigDecimal price;
        Object priceObj = doc.get("productPrice");
        if (priceObj instanceof BigDecimal) {
            price = (BigDecimal) priceObj;
        } else if (priceObj instanceof Double) {
            price = BigDecimal.valueOf((Double) priceObj);
        } else if (priceObj instanceof String) {
            try {
                price = new BigDecimal((String) priceObj);
            } catch (NumberFormatException e) {
                logger.error("Error parsing price for product '{}': {}", doc.getString("productName"), e.getMessage());
                price = BigDecimal.ZERO;
            }
        } else {
            price = BigDecimal.ZERO;
        }

        Products product = new Products(productId, doc.getString("productName"), doc.getString("productDescription"),
                stock, price, doc.getString("productType"), doc.getString("adminID"));
        logger.debug("Converted product document to product object: {}", product);
        return product;
    }


    public void updateProductStock(String productId, int newStock) {
        logger.info("Updating stock for productId: {}, newStock: {}", productId, newStock);
        productCollection.updateOne(eq("_id", new ObjectId(productId)), new Document("$set", new Document("productStock", newStock)));
        logger.debug("Stock updated successfully for productId: {}", productId);
    }
}
