
import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import com.example.asd2.Service.OrderService;
import com.example.asd2.Service.ProductService;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_shouldCreateOrderWhenStockIsSufficient() throws Exception {
        String customerId = "001";
        List<Cart.CartItem> items = new ArrayList<>();

        Cart.CartItem item = new Cart.CartItem();
        item.setProductId("p123");
        item.setProductName("Product1");
        item.setProductDescription("Description1");
        item.setProductPrice(100.0);
        item.setQuantity(2);
        items.add(item);

        Products product = new Products("p123", "Product1", "Description1", 10, new BigDecimal("100.0"), "Electronics", "admin123");
        Document customerDetails = new Document().append("fullName", "John Doe");

        when(productService.getProductByName("Product1")).thenReturn(Optional.of(product));

        orderService.createOrder(customerId, items, customerDetails);

        verify(mongoTemplate, times(1)).insert(any(Document.class), eq("Orders"));
    }

    @Test
    void createOrder_shouldThrowExceptionWhenStockIsInsufficient() {
        String customerId = "002";
        List<Cart.CartItem> items = new ArrayList<>();

        Cart.CartItem item = new Cart.CartItem();
        item.setProductId("p123");
        item.setProductName("Product1");
        item.setProductDescription("Description1");
        item.setProductPrice(100.0);
        item.setQuantity(20);
        items.add(item);

        Products product = new Products("p123", "Product1", "Description1", 10, new BigDecimal("100.0"), "Electronics", "admin123");

        when(productService.getProductByName("Product1")).thenReturn(Optional.of(product));

        assertThrows(Exception.class, () -> orderService.createOrder(customerId, items, new Document()));
    }
}
