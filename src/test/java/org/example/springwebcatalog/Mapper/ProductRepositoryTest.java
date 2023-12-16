package org.example.springwebcatalog.Mapper;

import org.example.springwebcatalog.Model.Product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("findProductByUuid - returns product with same ID")
    public void findProductByUuid_UuidShouldBeSame() {
        // Arrange
        Product product = new Product();
        productRepository.save(product);

        // Act
        Product foundProduct = productRepository.findProductByUuid(product.getUuid());

        // Assert
        assertThat(foundProduct.getUuid()).isEqualTo(product.getUuid());
    }

    @Test
    @DisplayName("findProductByNameContaining - returns products with name containing str")
    public void findProductByNameContaining_ShouldReturnIfContains() {
        // Arrange
        String productName = "TestProduct";
        Product product = new Product();
        product.setName(productName);
        productRepository.save(product);

        // Act
        List<Product> foundProducts = productRepository.findProductByNameContaining("Test");

        // Assert
        assertThat(foundProducts.size()).isOne();
        assertThat(foundProducts.getFirst().getName()).contains("Test");
    }

    @Test
    @DisplayName("findByTags - NoTags")
    public void findByTags_NoTags() {
        // Arrange
        Product product1 = new Product();
        Product product2 = new Product();
        productRepository.saveAll(List.of(product1, product2));

        // Act
        List<Product> foundProducts = productRepository.findByTags("tag1" + "tag2");

        // Assert
        assertThat(foundProducts.size()).isZero();
    }

}
