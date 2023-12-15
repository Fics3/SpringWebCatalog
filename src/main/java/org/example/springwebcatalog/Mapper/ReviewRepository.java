package org.example.springwebcatalog.Mapper;

import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewByProduct(Product product);
}
