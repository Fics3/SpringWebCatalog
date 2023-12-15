package org.example.springwebcatalog.Mapper;

import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Model.User.UserProducts.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUser(CustomUser customUser);

    CartItem findCartItemByProductAndUser(Product product, CustomUser customUser);

    CartItem findCartItemByUser(CustomUser customUser);

}
