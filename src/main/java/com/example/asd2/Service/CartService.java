package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final MongoCollection<Document> cartCollection;

    @Autowired
    private ProductService productService;

    @Autowired
    public CartService(MongoDatabase database) {
        this.cartCollection = database.getCollection("Cart");
    }

    public Cart getCartByCustomerId(String customerId) throws CartNotFoundException {
        logger.info("Fetching cart for customerId: {}", customerId);
        Document cartDoc = cartCollection.find(eq("customerId", customerId)).first();

        if (cartDoc == null) {
            throw new CartNotFoundException("Cart for customerId " + customerId + " not found");
        }

        return documentToCart(cartDoc);
    }

    private Cart documentToCart(Document doc) {
        Cart cart = new Cart();
        cart.setCustomerId(doc.getString("customerId"));

        List<Cart.CartItem> items = new ArrayList<>();
        double totalPrice = 0.0; // Initialize totalPrice for local calculation
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

                    // Update totalPrice based on the current product price and quantity
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
        cart.setTotalPrice(totalPrice); // Set calculated totalPrice

        return cart;
    }

    public void updateProductQuantity(String customerId, String productName, int newQuantity) throws CartNotFoundException {
        Document cartDoc = cartCollection.find(eq("customerId", customerId)).first();
        if (cartDoc == null) {
            throw new CartNotFoundException("Cart not found for customerId: " + customerId);
        }

        List<Document> items = cartDoc.getList("items", Document.class);

        if (items != null) {
            for (Document item : items) {
                if (item.getString("productName").equals(productName)) {
                    item.put("quantity", newQuantity);
                    break;
                }
            }
        }

        Document updatedCart = new Document("items", items);
        cartCollection.updateOne(eq("customerId", customerId), new Document("$set", updatedCart));
        logger.info("Updated quantity of product '{}' for customer '{}'", productName, customerId);
    }

    public void clearCart(String customerId) {
        try {
            Document update = new Document("$set", new Document("items", new ArrayList<>()));
            cartCollection.updateOne(eq("customerId", customerId), update);
            logger.info("Successfully cleared items in cart for customerId: {}", customerId);
        } catch (Exception e) {
            logger.error("Failed to clear items in cart for customerId: {}. Reason: {}", customerId, e.getMessage());
        }
    }
}
