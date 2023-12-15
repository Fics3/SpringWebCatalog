package org.example.springwebcatalog.Services.ServiceInterfaces;


import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(UUID uuid);

    void saveProduct(Product product);

    void editProduct(Product product);

    void deleteProduct(UUID uuid);

    void addProduct(Product product);

    List<Product> getProductByCustomUser(CustomUser customUser);

    List<Product> findProducts(String name);

    List<Product> sortProducts(List<Product> products, String sortField, String sortOrder);

    double getAverageRating(Product product);


}