package org.example.springwebcatalog.Model.User.UserProducts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;

import java.util.UUID;

@Getter
@Setter
@Entity
public class PurchasedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser customUser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    int quantity;

    public PurchasedItem(CustomUser customUser, Product product, int quantity) {
        this.customUser = customUser;
        this.product = product;
        this.quantity = quantity;
    }

    public PurchasedItem() {

    }
}
