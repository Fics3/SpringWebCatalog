package org.example.springwebcatalog.Repositories;

import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository customUserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("findAllByUser - Find All CartItems By User")
    public void findAllByUser_ShouldReturnAllCartItemsOfUser() {
        // Arrange
        CustomUser user = customUserRepository.save(new CustomUser());
        Product product = new Product();
        product.setUuid(UUID.fromString("42fa3910-9bba-4900-a297-8616d7026840"));
        productRepository.save(product); // Save the Product with the specified ID
        cartItemRepository.save(new CartItem(user, product, 1));

        // Act
        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);

        // Assert
        assertThat(cartItems).isNotEmpty();
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.getFirst().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("findCartItemByProductAndUser - Find CartItem By Product And User")
    public void findCartItemByProductAndUser_ShouldReturnCartItemByProductAndUser() {
        // Arrange
        CustomUser user = customUserRepository.save(new CustomUser());
        Product product = new Product();
        product.setUuid(UUID.fromString("6ee5f1ab-7c8f-4789-b9ec-55bd97978e4a"));
        productRepository.save(product); // Save the Product with the specified ID
        cartItemRepository.save(new CartItem(user, product, 1));

        // Act
        CartItem cartItem = cartItemRepository.findCartItemByProductAndUser(product, user);

        // Assert
        assertNotNull(cartItem);
        assertThat(cartItem.getProduct()).isEqualTo(product);
        assertThat(cartItem.getUser()).isEqualTo(user);
    }
}