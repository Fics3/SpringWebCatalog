package org.example.springwebcatalog.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.springwebcatalog.Mapper.ProductRepository;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Review;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Services.ServiceInterfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductManager implements ProductService {

    @Value("upload.dir")
    private static final String IMAGE_REPOSITORY = "src/main/resources/static/productImages";

    private final ProductRepository productRepository;

    @Autowired
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
    public void editProduct(Product product) {
        Optional<Product> existingProductOptional = productRepository.findById(product.getUuid());

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCount(product.getCount());

            productRepository.save(existingProduct);
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + product.getUuid());
        }
    }

    @Override
    public void deleteProduct(UUID uuid) {
        Product product = productRepository.findProductByUuid(uuid);
        product.setAvailable(false);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductByCustomUser(CustomUser customUser) {
        return productRepository.findProductBySeller(customUser);
    }

    @Override
    public List<Product> findProducts(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyList();
        }
        return productRepository.findProductByNameContaining(query);
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
