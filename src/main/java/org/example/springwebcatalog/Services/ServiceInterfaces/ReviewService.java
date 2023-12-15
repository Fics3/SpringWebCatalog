package org.example.springwebcatalog.Services.ServiceInterfaces;

import org.example.springwebcatalog.Mapper.ReviewRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {


    public List<Review> getReviewsByProduct(Product product);

    public void saveReview(Review review);
}
