package org.example.springwebcatalog.Model.User.UserProducts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartWrapper {
    private List<CartItem> userCart;

    public CartWrapper(List<CartItem> cartItems) {
        userCart = cartItems;
    }
}