package org.example.springwebcatalog.Mapper;


import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findProductByName(String name);

    List<Product> findProductByPriceLessThan(double price);

    List<Product> findProductBySeller(CustomUser customUser);

    Product findProductByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<Product> findProductByNameContainingIgnoreCase(String name);

    List<Product> findProductByNameContaining(String name);
}
