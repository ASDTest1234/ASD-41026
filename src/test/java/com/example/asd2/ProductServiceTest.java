
import com.example.asd2.Model.Products;
import com.example.asd2.Service.ProductService;
import com.example.asd2.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

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
        verify(productRepository, times(1)).findByProductName(productName);
    }
}
