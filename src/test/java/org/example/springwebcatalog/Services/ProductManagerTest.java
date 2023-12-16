package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Repositories.ProductRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Product.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ProductManagerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManager productManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getAllProducts - returns products in DB")
    void getAllProducts_sizeShouldBeEqualProductsCount() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(
                new Product(UUID.randomUUID(), "Product1", 20.0),
                new Product(UUID.randomUUID(), "Product2", 30.0)
        ));

        // Act
        List<Product> products = productManager.getAllProducts();

        // Assert
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("getProductById - returns product with needed id")
    void getProductById_shouldReturnSameProduct() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product mockProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(mockProduct));
        // Act
        Product result = productManager.getProductById(productId);

        // Assert
        assertThat(result).isNotNull().isEqualTo(mockProduct);
        assertThat(result.getUuid()).isEqualTo(mockProduct.getUuid());
        assertThat(result.getPrice()).isEqualTo(mockProduct.getPrice());
        assertThat(result.getName()).isEqualTo(mockProduct.getName());
    }

    @Test
    @DisplayName("deleteProduct - make Unavailable")
    void deleteProduct_ShouldSetProductUnavailable() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product1", 100.0);
        when(productRepository.findProductByUuid(productId)).thenReturn(product);

        // Act
        productManager.deleteProduct(productId);

        // Assert
        assertThat(product.isAvailable()).isFalse();
        Mockito.verify(productRepository).save(product);
    }

    @Test
    @DisplayName("sortProducts - ASC by price")
    void sortProducts_ASC_price() {
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Product1", 20.0),
                new Product(UUID.randomUUID(), "Product3", 15.0),
                new Product(UUID.randomUUID(), "Product2", 30.0)
        );

        // Act
        List<Product> sortedProducts = productManager.sortProducts(products, "price", "asc");

        // Assert
        assertThat(sortedProducts.get(0).getPrice()).isEqualTo(15.0);
        assertThat(sortedProducts.get(1).getPrice()).isEqualTo(20.0);
        assertThat(sortedProducts.get(2).getPrice()).isEqualTo(30.0);
    }


    @Test
    @DisplayName("sortProducts - DESC by price")
    void sortProducts_DESC_price() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Product1", 20.0),
                new Product(UUID.randomUUID(), "Product3", 15.0),
                new Product(UUID.randomUUID(), "Product2", 30.0)
        );

        // Act
        List<Product> sortedProducts = productManager.sortProducts(products, "price", "desc");

        // Assert
        assertThat(sortedProducts.get(0).getPrice()).isEqualTo(30.0);
        assertThat(sortedProducts.get(1).getPrice()).isEqualTo(20.0);
        assertThat(sortedProducts.get(2).getPrice()).isEqualTo(15.0);
    }

    @Test
    @DisplayName("sortProducts - ASC by name")
    void sortProducts_ASC_name() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Banana", 20.0),
                new Product(UUID.randomUUID(), "Apple", 15.0),
                new Product(UUID.randomUUID(), "Orange", 30.0)
        );

        // Act
        List<Product> sortedProducts = productManager.sortProducts(products, "name", "asc");

        // Assert
        assertThat(sortedProducts.get(0).getName()).isEqualTo("Apple");
        assertThat(sortedProducts.get(1).getName()).isEqualTo("Banana");
        assertThat(sortedProducts.get(2).getName()).isEqualTo("Orange");
    }

    @Test
    @DisplayName("sortProducts - DESC by name")
    void sortProducts_DESC_name() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Banana", 20.0),
                new Product(UUID.randomUUID(), "Apple", 15.0),
                new Product(UUID.randomUUID(), "Orange", 30.0)
        );

        // Act
        List<Product> sortedProducts = productManager.sortProducts(products, "name", "desc");

        // Assert
        assertThat(sortedProducts.get(0).getName()).isEqualTo("Orange");
        assertThat(sortedProducts.get(1).getName()).isEqualTo("Banana");
        assertThat(sortedProducts.get(2).getName()).isEqualTo("Apple");
    }

    @Test
    @DisplayName("getAverageRating - product with no reviews")
    void getAverageRating_noReviews() {
        // Arrange
        Product product = new Product(UUID.randomUUID(), "Product1", 20.0);

        // Act
        double averageRating = productManager.getAverageRating(product);

        // Assert
        assertThat(averageRating).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getAverageRating - product with one review")
    void getAverageRating_oneReview() {
        // Arrange
        Product product = new Product(UUID.randomUUID(), "Product1", 20.0);
        Review review = new Review(5); // Assuming a 5-star rating

        // Add the review to the product
        product.setReviewList(Arrays.asList(review));

        // Act
        double averageRating = productManager.getAverageRating(product);

        // Assert
        assertThat(averageRating).isEqualTo(5.0);
    }

    @Test
    @DisplayName("getAverageRating - product with multiple reviews")
    void getAverageRating_multipleReviews() {
        // Arrange
        Product product = new Product(UUID.randomUUID(), "Product1", 20.0);
        Review review1 = new Review(4);
        Review review2 = new Review(3);
        Review review3 = new Review(5);

        // Add the reviews to the product
        product.setReviewList(Arrays.asList(review1, review2, review3));

        // Act
        double averageRating = productManager.getAverageRating(product);

        // Assert
        assertThat(averageRating).isEqualTo(4.0); // (4 + 3 + 5) / 3 = 4.0
    }


}
