package org.example.springwebcatalog.Services;

import jakarta.transaction.Transactional;
import org.example.springwebcatalog.Mapper.ReviewRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Product.Review;
import org.example.springwebcatalog.Services.ServiceInterfaces.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewManager implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewManager(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }
}
