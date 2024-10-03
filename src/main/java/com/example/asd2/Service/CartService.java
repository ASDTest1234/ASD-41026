package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.MongoDBConfig;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final MongoCollection<Document> cartCollection;

    public CartService() {
        MongoDatabase database = MongoDBConfig.getDatabase("ASD");  // 确保数据库名为 "ASD"
        this.cartCollection = database.getCollection("Cart");  // 集合名为 "Cart"
    }

    // 获取购物车
    public Cart getCartByCustomerId(String customerId) throws CartNotFoundException {
        logger.info("Fetching cart for customerId: {}", customerId);
        Document cartDoc = cartCollection.find(eq("customerId", customerId)).first();
        if (cartDoc == null) {
            logger.warn("No cart found for customerId: {}", customerId);
            throw new CartNotFoundException("Cart for customerId " + customerId + " not found");
        }
        logger.info("Found cart: {}", cartDoc.toJson());

        return documentToCart(cartDoc);
    }

    // 更新购物车中的商品数量
    public void updateProductQuantity(String customerId, String productId, int newQuantity) throws CartNotFoundException {
        logger.info("Updating product quantity for productId: {} to {}", productId, newQuantity);
        Document cartDoc = cartCollection.find(eq("customerId", customerId)).first();
        if (cartDoc == null) {
            logger.warn("No cart found for customerId: {}", customerId);
            throw new CartNotFoundException("Cart for customerId " + customerId + " not found");
        }

        // 获取并更新商品数量
        List<Document> items = cartDoc.getList("items", Document.class);
        boolean productFound = false;
        double totalPrice = 0.0;
        for (Document item : items) {
            if (item.getString("productId").equals(productId)) {
                item.put("quantity", newQuantity);  // 更新商品数量
                productFound = true;
            }
            totalPrice += item.getDouble("productPrice") * item.getInteger("quantity");
        }

        if (!productFound) {
            logger.warn("Product with productId: {} not found in cart", productId);
            throw new CartNotFoundException("Product with productId " + productId + " not found in cart");
        }

        // 更新购物车总价
        cartDoc.put("totalPrice", totalPrice);
        cartCollection.updateOne(eq("customerId", customerId), new Document("$set", cartDoc));
        logger.info("Updated product quantity for customerId: {}, new total price: {}", customerId, totalPrice);
    }

    // 将 MongoDB 文档转换为 Cart 对象
    private Cart documentToCart(Document doc) {
        Cart cart = new Cart();
        cart.setId(doc.getString("_id"));
        cart.setCustomerId(doc.getString("customerId"));
        cart.setTotalPrice(doc.getDouble("totalPrice"));

        // 处理 items 列表
        List<Cart.CartItem> items = new ArrayList<>();
        List<Document> itemDocs = (List<Document>) doc.get("items");
        for (Document itemDoc : itemDocs) {
            Cart.CartItem cartItem = new Cart.CartItem();
            cartItem.setProductId(itemDoc.getString("productId"));
            cartItem.setProductName(itemDoc.getString("productName"));
            cartItem.setProductDescription(itemDoc.getString("productDescription"));
            cartItem.setProductPrice(itemDoc.getDouble("productPrice"));
            cartItem.setQuantity(itemDoc.getInteger("quantity", 1));  // 默认数量为 1
            items.add(cartItem);
        }
        cart.setItems(items);

        logger.info("Mapped Cart object: {}", cart);
        return cart;
    }
}
