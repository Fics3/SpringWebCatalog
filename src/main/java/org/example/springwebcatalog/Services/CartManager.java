package org.example.springwebcatalog.Services;

import jakarta.transaction.Transactional;
import org.example.springwebcatalog.Repositories.CartItemRepository;
import org.example.springwebcatalog.Repositories.PurchasedItemRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.example.springwebcatalog.Model.User.UserProducts.PurchasedItem;
import org.example.springwebcatalog.Services.ServiceInterfaces.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartManager implements CartService {

    private final CartItemRepository cartItemRepository;
    private final PurchasedItemRepository purchasedItemRepository;

    public CartManager(CartItemRepository cartItemRepository, PurchasedItemRepository purchasedItemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.purchasedItemRepository = purchasedItemRepository;
    }

    @Override
    @Transactional
    public void addToCart(CustomUser customUser, Product product, int quantity) {
        CartItem cartItem = cartItemRepository.findCartItemByProductAndUser(product, customUser);

        if (cartItem != null) {
            cartItem.setQuantity(Math.min(product.getCount(), quantity + cartItem.getQuantity()));
        } else {
            if (product.getCount() > 0) {
                cartItem = new CartItem(customUser, product, Math.min(product.getCount(), quantity));
                cartItemRepository.save(cartItem);
            }
        }
    }

    @Override
    @Transactional
    public void removeFromCart(CustomUser customUser) {
        List<CartItem> cartItems = cartItemRepository.findAllByUser(customUser);

        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
        }
    }


    @Override
    public double getTotalSum(CustomUser customUser) {
        return customUser.getCartItems().stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    @Override
    @Transactional
    public void updateCartItem(CartItem cartItem) {
        cartItem.setQuantity(Math.min(cartItem.getProduct().getCount(), cartItem.getQuantity()));
        cartItemRepository.delete(cartItem);
        if (cartItem.getQuantity() > 0) {
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public void purchaseAndCleanCart(List<CartItem> cartItems, CustomUser customUser) {
        if (cartItems != null) {
            List<PurchasedItem> purchasedItems = cartItems.stream()
                    .map(cartItem -> {
                        PurchasedItem purchasedItem = buyItem(cartItem, customUser);
                        reduceProductCount(cartItem, purchasedItem);
                        return purchasedItem;
                    }).toList();
            cartItems.clear();
            purchasedItemRepository.saveAll(purchasedItems);
            cartItemRepository.saveAll(cartItems);
        }
    }

    private PurchasedItem buyItem(CartItem cartItem, CustomUser customUser) {
        return new PurchasedItem(customUser,
                cartItem.getProduct(),
                Math.min(cartItem.getProduct().getCount(),
                        cartItem.getQuantity()));
    }

    private void reduceProductCount(CartItem cartItem, PurchasedItem purchasedItem) {
        int productQuantity = cartItem.getProduct().getCount();
        int purchasedQuantity = purchasedItem.getQuantity();
        cartItem.getProduct().setCount(productQuantity - purchasedQuantity);
    }

}
