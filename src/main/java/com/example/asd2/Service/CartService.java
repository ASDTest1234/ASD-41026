package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import com.example.asd2.Service.CartNotFoundException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing Cart operations.
 */
@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final MongoTemplate mongoTemplate;
    private final ProductService productService;

    @Autowired
    public CartService(MongoTemplate mongoTemplate, ProductService productService) {
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
    }

    /**
     * Adds a product to the cart or updates its quantity if it already exists.
     */
    public void addToCart(String customerId, String productId, int quantity) {
        logger.info("Attempting to add product to cart - Customer ID: {}, Product ID: {}, Quantity: {}",
                customerId, productId, quantity);

        Query query = new Query(Criteria.where("customerId").is(customerId));
        Cart cart = mongoTemplate.findOne(query, Cart.class, "Cart");

        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setItems(new ArrayList<>());
            logger.info("Creating a new cart for customer ID: {}", customerId);
        }

        Optional<Cart.CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            Cart.CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            logger.info("Updated quantity of product '{}' to {} for customer '{}'",
                    productId, item.getQuantity(), customerId);
        } else {
            Optional<Products> productOpt = productService.getProductById(productId);
            if (productOpt.isPresent()) {
                Products product = productOpt.get();
                Cart.CartItem newItem = new Cart.CartItem();
                newItem.setProductId(product.getProduct_Id());
                newItem.setProductName(product.getProductName());
                newItem.setProductDescription(product.getProductDescription());
                newItem.setProductPrice(product.getProductPrice().doubleValue());
                newItem.setQuantity(quantity);
                newItem.setStockSufficient(product.getProductStock() >= quantity);
                cart.getItems().add(newItem);
                logger.info("Added product '{}' with quantity {} to cart for customer '{}'",
                        productId, quantity, customerId);
            } else {
                logger.warn("Product '{}' not found, cannot add to cart for customer '{}'", productId, customerId);
                return;
            }
        }

        mongoTemplate.save(cart, "Cart");
        logger.info("Cart updated/saved successfully for customer ID: {}", customerId);
    }

    /**
     * Retrieves the cart by customer ID.
     */
    public Cart getCartByCustomerId(String customerId) throws CartNotFoundException {
        logger.info("Fetching cart for customerId: {}", customerId);

        Query query = new Query(Criteria.where("customerId").is(customerId));
        Document cartDoc = mongoTemplate.findOne(query, Document.class, "Cart");

        if (cartDoc == null) {
            throw new CartNotFoundException("Cart for customerId " + customerId + " not found");
        }

        return documentToCart(cartDoc);
    }

    /**
     * Converts a MongoDB Document into a Cart object.
     */
    private Cart documentToCart(Document doc) {
        Cart cart = new Cart();
        cart.setCustomerId(doc.getString("customerId"));
        List<Cart.CartItem> items = new ArrayList<>();
        double totalPrice = 0.0;

        List<Document> itemDocs = (List<Document>) doc.get("items");
        if (itemDocs != null) {
            for (Document itemDoc : itemDocs) {
                Cart.CartItem cartItem = new Cart.CartItem();
                String productName = itemDoc.getString("productName");
                int quantity = itemDoc.getInteger("quantity", 1);
                Optional<Products> productOpt = productService.getProductByName(productName);

                if (productOpt.isPresent()) {
                    Products product = productOpt.get();
                    cartItem.setProductName(product.getProductName());
                    cartItem.setProductDescription(product.getProductDescription());
                    cartItem.setProductPrice(product.getProductPrice().doubleValue());
                    cartItem.setQuantity(quantity);
                    cartItem.setStockSufficient(product.getProductStock() >= quantity);
                    totalPrice += product.getProductPrice().doubleValue() * quantity;
                } else {
                    cartItem.setProductName(productName);
                    cartItem.setProductDescription("Description not available");
                    cartItem.setProductPrice(0.0);
                    cartItem.setQuantity(quantity);
                    cartItem.setStockSufficient(false);
                }
                items.add(cartItem);
            }
        }
        cart.setItems(items);
        cart.setTotalPrice(totalPrice);
        return cart;
    }

    /**
     * Updates the quantity of a product in the cart.
     */
    public void updateProductQuantity(String customerId, String productName, int newQuantity) throws CartNotFoundException {
        Query query = new Query(Criteria.where("customerId").is(customerId).and("items.productName").is(productName));
        Update update = new Update().set("items.$.quantity", newQuantity);

        mongoTemplate.updateFirst(query, update, "Cart");
        logger.info("Updated quantity of product '{}' for customer '{}'", productName, customerId);
    }

    /**
     * Clears the items from the cart.
     */
    public void clearCart(String customerId) {
        Query query = new Query(Criteria.where("customerId").is(customerId));
        Update update = new Update().set("items", new ArrayList<>());
        mongoTemplate.updateFirst(query, update, "Cart");
        logger.info("Successfully cleared items in cart for customerId: {}", customerId);
    }

    public void removeProductFromCart(String customerId, String productName) throws CartNotFoundException {
        Query query = new Query(Criteria.where("customerId").is(customerId).and("items.productName").is(productName));
        Update update = new Update().pull("items", Query.query(Criteria.where("productName").is(productName)));
        mongoTemplate.updateFirst(query, update, "Cart");
        logger.info("Removed product '{}' from cart for customer '{}'", productName, customerId);
    }


}
