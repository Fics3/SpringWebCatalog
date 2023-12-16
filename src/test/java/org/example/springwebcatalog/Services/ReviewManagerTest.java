package org.example.springwebcatalog.Services;

import org.example.springwebcatalog.Repositories.ReviewRepository;
import org.example.springwebcatalog.Model.Product.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class ReviewManagerTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewManager reviewManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("saveReview - should save")
    public void saveReview_ShouldCallRepositorySave() {
        // Arrange
        Review review = new Review();

        // Act
        reviewManager.saveReview(review);

        // Assert
        verify(reviewRepository).save(review);
    }
}
