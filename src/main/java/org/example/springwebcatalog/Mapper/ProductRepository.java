package org.example.springwebcatalog.Mapper;


import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {


    List<Product> findProductBySeller(CustomUser customUser);

    Product findProductByUuid(UUID uuid);

    List<Product> findProductByNameContaining(String name);

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.name IN :tagNames")
    List<Product> findByTags(@Param("tagNames") String tagNames);
}
