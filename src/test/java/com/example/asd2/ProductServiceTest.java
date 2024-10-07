import com.example.asd2.Model.Products;
import com.example.asd2.Service.ProductService;
import com.example.asd2.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceTest.class);

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductByName_shouldReturnProductWhenExists() {
        String productName = "Test Product";
        Products product = new Products("1", productName, "Test Description", 10, BigDecimal.valueOf(100), "Electronics", "admin123");

        when(productRepository.findByProductName(productName)).thenReturn(Optional.of(product));

        Optional<Products> result = productService.getProductByName(productName);

        assertTrue(result.isPresent());
        logger.info("Test getProductByName_shouldReturnProductWhenExists: Product '{}' retrieved successfully.", productName);
        verify(productRepository, times(1)).findByProductName(productName);
    }
}
