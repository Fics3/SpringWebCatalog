package org.example.springwebcatalog.Services.ServiceInterfaces;

import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {


    void addToCart(CustomUser customUser, Product product, int quantity);

    void removeFromCart(CustomUser customUser);

    double getTotalSum(CustomUser customUser);

    void updateCartItem(CartItem cartItem);

    void purchaseAndCleanCart(List<CartItem> cartItems, CustomUser customUser);

}
