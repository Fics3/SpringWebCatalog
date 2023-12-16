package org.example.springwebcatalog.Model.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.springwebcatalog.Model.User.CustomUser;

import java.util.*;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Product() {
        uuid = UUID.randomUUID();
        available = true;
    }

    public Product(String name, String description, double price, int count) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.count = count;
        available = true;
    }

    //TEST
    public Product(UUID uuid, String name, double price) {
        this.uuid = uuid;
        this.name = name;
        this.price = price;
    }

    public Product(String product1, double v, int count) {
        this.name = product1;
        this.price = v;
        this.count = count;
    }

    public void addReview(Review review) {
        reviewList.add(review);
        review.setProduct(this);
    }

}
