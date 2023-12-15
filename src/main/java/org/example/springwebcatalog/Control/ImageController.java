package org.example.springwebcatalog.Control;

import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Mapper.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class ImageController {
    private final ProductRepository productRepository;

    public ImageController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/images/{productId}")
    @ResponseBody
    public byte[] getImage(@PathVariable UUID productId) {
        // Получите изображение из вашего репозитория
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null && product.getImage() != null) {
            return product.getImage();
        } else {
            return new byte[0];
        }
    }
}
