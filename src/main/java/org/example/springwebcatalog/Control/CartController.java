package org.example.springwebcatalog.Control;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.example.springwebcatalog.Model.User.UserProducts.CartWrapper;
import org.example.springwebcatalog.Services.ServiceInterfaces.CartService;
import org.example.springwebcatalog.Services.ServiceInterfaces.ProductService;
import org.example.springwebcatalog.Services.ServiceInterfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class CartController {


    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;


    public CartController(ProductService productService, UserService userService, CartService cartService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }


    @GetMapping("/cart")
    @Transactional
    public String getCart(Model model, Principal principal) {
        CustomUser customUser = userService.findUserByName(principal.getName());
        CartWrapper cartWrapper = new CartWrapper(customUser.getCartItems());
        model.addAttribute("userCart", cartWrapper);
        model.addAttribute("userCartTotal", cartService.getTotalSum(customUser));

        model.addAttribute("username", principal.getName());
        model.addAttribute("contentTemplate", "/product/cart");
        model.addAttribute("loggedIn", true);
        model.addAttribute("pageTitle", "Your cart");


        return "interface";
    }

    @PostMapping("/add/{id}")
    public String addToTheCart(@PathVariable UUID id, @RequestParam("quantity") int quantity,
                               Principal principal,
                               HttpServletRequest httpServletRequest) {
        Product product = productService.getProductById(id);
        CustomUser customUser = userService.findUserByName(principal.getName());
        cartService.addToCart(customUser, product, quantity);
        String referer = httpServletRequest.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/cart/update")
    public String updateCart(@ModelAttribute("userCart") CartWrapper cartWrapper, Principal principal) {
        List<CartItem> userCart = cartWrapper.getUserCart();

        if (userCart != null) {
            CustomUser customUser = userService.findUserByName(principal.getName());

            cartService.removeFromCart(customUser);

            userCart.forEach(cartItem -> {
                cartItem.setUser(customUser);
                cartService.updateCartItem(cartItem);
            });

        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/buy")
    public String buyCart(Principal principal) {
        CustomUser customUser = userService.findUserByName(principal.getName());
        var userCart = customUser.getCartItems();

        cartService.purchaseAndCleanCart(userCart, customUser);

        return "redirect:/cart";
    }


}
