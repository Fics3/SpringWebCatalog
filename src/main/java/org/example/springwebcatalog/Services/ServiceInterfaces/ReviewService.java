package org.example.springwebcatalog.Services.ServiceInterfaces;

import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Product.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {



    void saveReview(Review review);
}
