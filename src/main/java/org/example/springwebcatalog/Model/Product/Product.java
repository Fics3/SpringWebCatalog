package org.example.springwebcatalog.Model.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.springwebcatalog.Model.Review;
import org.example.springwebcatalog.Model.User.CustomUser;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Product {

    private String name;
    private String description;
    private double price;
    private int count;
    private boolean available;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private CustomUser seller;

    @Id
    private UUID uuid;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList;

    public Product() {
        uuid = UUID.randomUUID();
        available = true;
    }

    public Product(String name, String description, double price, int count) {
        this.name = name;
        this.description = description;
        this.price = price;
        uuid = UUID.randomUUID();
        this.count = count;
        available = true;
    }

}
