package org.example.springwebcatalog.Model.User.UserProducts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;

import java.util.UUID;


@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    private int quantity;

    public CartItem() {

    }

    public CartItem(CustomUser user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

}