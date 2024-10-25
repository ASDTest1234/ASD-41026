import com.example.asd2.Model.Cart;
import com.example.asd2.Model.Products;
import com.example.asd2.Model.Order;
import com.example.asd2.Service.OrderService;
import com.example.asd2.Service.ProductService;
import com.example.asd2.repository.OrderRepository;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

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
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());


        orderService.createOrder(customerId, items, customerDetails);

        logger.info("Test createOrder_shouldCreateOrderWhenStockIsSufficient: Order created successfully for customerId {}", customerId);


        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowExceptionWhenStockIsInsufficient() {
        // 设置客户ID和购物车
        String customerId = "002";
        List<Cart.CartItem> items = new ArrayList<>();

        Cart.CartItem item = new Cart.CartItem();
        item.setProductId("p123");
        item.setProductName("Product1");
        item.setProductDescription("Description1");
        item.setProductPrice(100.0);
        item.setQuantity(10);
        items.add(item);


        Products product = new Products("p123", "Product1", "Description1", 5, new BigDecimal("100.0"), "Electronics", "admin123");


        when(productService.getProductByName("Product1")).thenReturn(Optional.of(product));


        assertThrows(Exception.class, () -> {
            orderService.createOrder(customerId, items, new Document());
        });

        verify(orderRepository, never()).save(any());
    }
}
