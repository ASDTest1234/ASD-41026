package com.example.asd2.Service;

import com.example.asd2.Model.Products;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Decimal128;
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

        String productId = doc.getObjectId("_id").toHexString();

        BigDecimal price = doc.get("productPrice", Decimal128.class).bigDecimalValue();
        int stock = doc.getInteger("productStock", 0);

        return new Products(
                productId,
                doc.getString("productName"),
                doc.getString("productDescription"),
                stock,
                price,
                doc.getString("productType"),
                doc.getString("adminID")
        );
    }

    public void updateProductStock(String productName, int newStock) {
        try {
            Document update = new Document("$set", new Document("productStock", newStock));
            productCollection.updateOne(eq("productName", productName), update);
            logger.info("Successfully updated stock for product: {}, newStock: {}", productName, newStock);
        } catch (Exception e) {
            logger.error("Error updating stock for product: {}. Reason: {}", productName, e.getMessage());
        }
    }
}
