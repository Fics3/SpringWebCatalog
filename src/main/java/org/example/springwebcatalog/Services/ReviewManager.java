package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Mapper.ReviewRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Review;
import org.example.springwebcatalog.Services.ServiceInterfaces.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewManager implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewManager(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findReviewByProduct(product);
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }
}
