package com.example.asd2.Service;

import com.example.asd2.Model.Products;
import com.example.asd2.repository.ProductRepository;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service for managing Product operations, utilizing both MongoTemplate and ProductRepository.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final MongoTemplate mongoTemplate;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(MongoTemplate mongoTemplate, ProductRepository productRepository) {
        this.mongoTemplate = mongoTemplate;
        this.productRepository = productRepository;
    }

    /**
     * Finds a product by name using ProductRepository.
     *
     * @param productName Name of the product.
     * @return Optional of Products
     */
    public Optional<Products> getProductByName(String productName) {
        logger.info("Fetching product by name: {}", productName);
        return productRepository.findByProductName(productName);
    }

    /**
     * Retrieves all products.
     *
     * @return List of Products.
     */
    public List<Products> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    public Products addProduct(Products product) {
        return productRepository.save(product);
    }

    /**
     * Converts a MongoDB document to Products.
     *
     * @param doc MongoDB Document.
     * @return Products
     */
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

    /**
     * Updates the stock of a product by product name using MongoTemplate.
     *
     * @param productName Name of the product.
     * @param newStock New stock value to set.
     */
    public void updateProductStock(String productName, int newStock) {
        logger.info("Updating stock for product: {}, new stock: {}", productName, newStock);

        Query query = new Query(Criteria.where("productName").is(productName));
        Update update = new Update().set("productStock", newStock);
        mongoTemplate.updateFirst(query, update, "Product");
    }

    /**
     * Retrieves a product by name with custom query using MongoTemplate.
     *
     * @param productName Name of the product.
     * @return Optional of Products
     */
    public Optional<Products> getProductByNameCustomQuery(String productName) {
        logger.info("Fetching product by name using custom query: {}", productName);

        Query query = new Query(Criteria.where("productName").is(productName));
        Document productDoc = mongoTemplate.findOne(query, Document.class, "Product");

        if (productDoc == null) {
            logger.warn("Product not found for name: {}", productName);
            return Optional.empty();
        }

        Products product = documentToProduct(productDoc);
        logger.debug("Retrieved product details: {}", product);
        return Optional.of(product);
    }

    public Optional<Products> getProductById(String productId) {
        logger.info("Fetching product by ID: {}", productId);

        Query query = new Query(Criteria.where("_id").is(new ObjectId(productId)));
        return Optional.ofNullable(mongoTemplate.findOne(query, Products.class, "Product"));
    }

    public boolean deleteProductByName(String productName) {
        Optional<Products> product = productRepository.findByProductName(productName);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        }
        return false;
    }

}
