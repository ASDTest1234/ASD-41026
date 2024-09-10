package com.example.asd2.Service;

import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Product;
import com.example.asd2.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    /**
     * Fetches the cart for the specified customer by their customer ID.
     *
     * @param customerId The ID of the customer.
     * @return Cart object containing the list of products and the total price.
     */
    public Cart getCartByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        logger.info("Fetching cart for customer ID: {}", customerId);
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart == null) {
            logger.warn("Cart not found for customer ID: {}", customerId);
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }
        return cart;
    }

    /**
     * Clears the cart for a specific customer.
     *
     * @param customerId The ID of the customer.
     */
    public void clearCart(String customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart != null) {
            clearProductsInCart(cart);
            updateCartTotalPrice(cart);
            cartRepository.save(cart);
            logger.info("Cart cleared for customer ID: {}", customerId);
        } else {
            logger.warn("Attempt to clear a cart that doesn't exist for customer ID: {}", customerId);
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }
    }

    /**
     * Adds a product to the cart.
     *
     * @param customerId The ID of the customer.
     * @param product    The product to be added.
     */
    public void addProductToCart(String customerId, Product product) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        if (cart != null) {
            cart.getProducts().add(product);
            updateCartTotalPrice(cart);
            cartRepository.save(cart);
            logger.info("Product added to cart for customer ID: {}", customerId);
        } else {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }
    }

    /**
     * Clears all products in the cart.
     *
     * @param cart The cart object.
     */
    private void clearProductsInCart(Cart cart) {
        if (cart.getProducts() != null) {
            cart.getProducts().clear();
        }
    }

    /**
     * Recalculates and updates the total price of the cart.
     *
     * @param cart The cart object.
     */
    private void updateCartTotalPrice(Cart cart) {
        double totalPrice = cart.getProducts().stream()
                .mapToDouble(Product::getProductPrice)
                .sum();
        cart.setTotalPrice(totalPrice);
    }
}
