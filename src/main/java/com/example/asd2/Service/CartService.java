package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
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
            logger.error("Cart not found for customerId: {}", customerId);
            throw new CartNotFoundException("Cart for customerId " + customerId + " not found");
        }

        Cart cart = documentToCart(cartDoc);
        logger.debug("Retrieved cart: {}", cart);
        return cart;
    }

    private Cart documentToCart(Document doc) {
        logger.info("Converting document to Cart: {}", doc.toJson());
        Cart cart = new Cart();
        Object idObj = doc.get("_id");
        if (idObj instanceof ObjectId) {
            cart.setId(((ObjectId) idObj).toHexString());
        } else if (idObj instanceof String) {
            cart.setId((String) idObj);
        }
        cart.setCustomerId(doc.getString("customerId"));

        List<Cart.CartItem> items = new ArrayList<>();
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

                    // 处理库存并检查是否充足
                    Object stockObj = product.getProductStock();
                    int stock = 0;
                    if (stockObj instanceof Integer) {
                        stock = (Integer) stockObj;
                    } else if (stockObj instanceof Long) {
                        stock = ((Long) stockObj).intValue();
                    }
                    cartItem.setStockSufficient(stock >= quantity);

                } else {
                    logger.warn("Product not found for productName: {}", productName);
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
        cart.setTotalPrice(doc.get("totalPrice") != null ? doc.getDouble("totalPrice") : 0.0);
        logger.debug("Final Cart Data - Total Price: {}, Items: {}", cart.getTotalPrice(), items);
        return cart;
    }



    public void updateProductQuantity(String customerId, String productName, int newQuantity) throws CartNotFoundException {
        logger.info("Updating product quantity for customerId: {}, productName: {}, newQuantity: {}", customerId, productName, newQuantity);
        Document cartDoc = cartCollection.find(eq("customerId", customerId)).first();

        if (cartDoc == null) {
            logger.error("Cart not found for customerId: {}", customerId);
            throw new CartNotFoundException("Cart not found for customerId: " + customerId);
        }

        List<Document> items = cartDoc.getList("items", Document.class);
        if (items != null) {
            for (Document item : items) {
                if (item.getString("productName").equals(productName)) {
                    item.put("quantity", newQuantity);
                    logger.debug("Updated item quantity for product: {}, new quantity: {}", productName, newQuantity);
                }
            }
        }

        cartCollection.updateOne(eq("customerId", customerId), new Document("$set", new Document("items", items)));
        logger.info("Successfully updated quantity for customerId: {}, productName: {}", customerId, productName);
    }



    public void clearCart(String customerId) {  // 确保 clearCart 是 public
        cartCollection.deleteOne(eq("customerId", customerId));
    }

}