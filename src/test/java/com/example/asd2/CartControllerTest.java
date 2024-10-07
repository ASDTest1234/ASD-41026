
import com.example.asd2.Controller.CartController;
import com.example.asd2.Model.Cart;
import com.example.asd2.Service.CartNotFoundException;
import com.example.asd2.Service.CartService;
import com.example.asd2.Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCart_shouldReturnCartWhenExists() throws CartNotFoundException {
        String customerId = "001";
        Cart cart = new Cart();
        cart.setCustomerId(customerId);

        when(cartService.getCartByCustomerId(customerId)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.getCart(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    void getCart_shouldReturnNotFoundWhenCartDoesNotExist() throws CartNotFoundException {
        String customerId = "002";

        when(cartService.getCartByCustomerId(customerId)).thenThrow(new CartNotFoundException("Cart not found"));

        ResponseEntity<Cart> response = cartController.getCart(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProductQuantity_shouldReturnOkOnSuccess() throws CartNotFoundException {
        String customerId = "003";
        String productName = "Test Product";
        int quantity = 2;

        doNothing().when(cartService).updateProductQuantity(customerId, productName, quantity);

        ResponseEntity<String> response = cartController.updateProductQuantity(customerId, productName, quantity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Quantity updated successfully", response.getBody());
    }

    @Test
    void updateProductQuantity_shouldReturnNotFoundWhenCartDoesNotExist() throws CartNotFoundException {
        String customerId = "004";
        String productName = "Test Product";
        int quantity = 3;

        doThrow(new CartNotFoundException("Cart not found")).when(cartService).updateProductQuantity(customerId, productName, quantity);

        ResponseEntity<String> response = cartController.updateProductQuantity(customerId, productName, quantity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void removeProductFromCart_shouldReturnOkOnSuccess() throws CartNotFoundException {
        String customerId = "005";
        String productName = "Test Product";

        doNothing().when(cartService).removeProductFromCart(customerId, productName);

        ResponseEntity<String> response = cartController.removeProductFromCart(customerId, productName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product removed successfully", response.getBody());
    }
}
