package org.example.springwebcatalog.Services;

import jakarta.transaction.Transactional;
import org.example.springwebcatalog.Repositories.ProductRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Product.Review;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Services.ServiceInterfaces.ProductService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ProductManager implements ProductService {

    private final ProductRepository productRepository;

    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(UUID uuid) {
        return productRepository.findById(uuid).orElse(null);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    @Override
    public void deleteProduct(UUID uuid) {
        Product product = productRepository.findProductByUuid(uuid);
        product.setAvailable(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public List<Product> getProductByCustomUser(CustomUser customUser) {
        return productRepository.findProductBySeller(customUser);
    }

    @Override
    @Transactional
    public List<Product> findProducts(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyList();
        }
        var result = productRepository.findProductByNameContaining(query);
        return result.isEmpty() ? productRepository.findByTags(query) : result;
    }

    @Override
    public List<Product> sortProducts(List<Product> products, String sortField, String sortOrder) {

        Comparator<Product> productComparator = getComparator(sortField, sortOrder);

        products.sort(productComparator);

        return products;
    }

    @Override
    public double getAverageRating(Product product) {
        double avgRating = product.getReviewList().stream()
                .mapToInt(Review::getRating)
                .average().orElse(0.0);

        return Math.round(avgRating * 10.0) / 10.0;
    }


    private Comparator<Product> getComparator(String sortField, String sortOrder) {
        Comparator<Product> productComparator;

        productComparator = switch (sortField) {
            case "name" -> Comparator.comparing(Product::getName);
            case "price" -> Comparator.comparing(Product::getPrice);
            default -> Comparator.comparing(Product::isAvailable);
        };

        if (sortOrder.equals("desc")) {
            productComparator = productComparator.reversed();
        }

        return productComparator;
    }

}
