package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Repositories.CartItemRepository;
import org.example.springwebcatalog.Repositories.PurchasedItemRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.Role;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartManagerTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private PurchasedItemRepository purchasedItemRepository;

    @InjectMocks
    private CartManager cartManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("addToCart - existing cart item, update quantity")
    void addToCart_existingCartItem_updateQuantity() {
        // Arrange
        CustomUser customUser = new CustomUser("user", "password", Role.USER);
        Product product = new Product("Product1", 10.0, 5);
        CartItem existingCartItem = new CartItem(customUser, product, 2);

        when(cartItemRepository.findCartItemByProductAndUser(product, customUser))
                .thenReturn(existingCartItem);

        // Act
        cartManager.addToCart(customUser, product, 3);

        // Query the cart item again
        CartItem updatedCartItem = cartItemRepository.findCartItemByProductAndUser(product, customUser);

        // Assert
        verify(cartItemRepository, times(2)).findCartItemByProductAndUser(any(), any());
        assertThat(updatedCartItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("addToCart - new cart item, save to cart")
    void addToCart_newCartItem_saveToCart() {
        // Arrange
        CustomUser customUser = new CustomUser("user", "password", Role.USER);
        Product product = new Product("Product1", 10.0, 5);

        when(cartItemRepository.findCartItemByProductAndUser(product, customUser))
                .thenReturn(null);

        // Act
        cartManager.addToCart(customUser, product, 3);

        // Assert
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("removeFromCart - remove all cart items")
    void removeFromCart_removeAllCartItems() {
        // Arrange
        CustomUser customUser = new CustomUser("user", "password", Role.USER);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(customUser, new Product("Product1", 10.0, 5), 2));
        cartItems.add(new CartItem(customUser, new Product("Product2", 15.0, 3), 1));

        when(cartItemRepository.findAllByUser(customUser)).thenReturn(cartItems);

        // Act
        cartManager.removeFromCart(customUser);

        // Assert
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
    }

    @Test
    @DisplayName("getTotalSum - calculate total sum of cart items")
    void getTotalSum_calculateTotalSum() {
        // Arrange
        CustomUser customUser = mock(CustomUser.class); // Create a mock for CustomUser
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(customUser, new Product("Product1", 10.0, 2), 2));
        cartItems.add(new CartItem(customUser, new Product("Product2", 15.0, 3), 1));

        when(customUser.getCartItems()).thenReturn(cartItems); // Configure the behavior

        // Act
        double totalSum = cartManager.getTotalSum(customUser);

        // Assert
        assertThat(totalSum).isEqualTo(2 * 10.0 + 1 * 15.0);
    }

    @Test
    @DisplayName("updateCartItem - update cart item quantity and save")
    void updateCartItem_updateQuantityAndSave() {
        // Arrange
        CartItem cartItem = new CartItem(new CustomUser("user", "password", Role.USER),
                new Product("Product1", 10.0, 5), 2);

        // Act
        cartManager.updateCartItem(cartItem);

        // Assert
        assertThat(cartItem.getQuantity()).isEqualTo(2);
        verify(cartItemRepository, times(1)).delete(cartItem);
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    @DisplayName("purchaseAndCleanCart - purchase items and clear cart")
    void purchaseAndCleanCart_purchaseItemsAndClearCart() {
        // Arrange
        CustomUser customUser = new CustomUser("user", "password", Role.USER);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(customUser, new Product("Product1", 10.0, 5), 2));
        cartItems.add(new CartItem(customUser, new Product("Product2", 15.0, 3), 1));

        // Ensure cartItems is not empty
        assertThat(cartItems).isNotEmpty();

        // Act
        cartManager.purchaseAndCleanCart(cartItems, customUser);

        // Query the cart items again after the purchaseAndCleanCart operation
        List<CartItem> remainingCartItems = cartItemRepository.findAllByUser(customUser);

        // Assert
        assertThat(remainingCartItems).isEmpty();
    }



}
