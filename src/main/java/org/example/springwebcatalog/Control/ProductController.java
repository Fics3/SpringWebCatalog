package org.example.springwebcatalog.Control;

import jakarta.transaction.Transactional;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Product.Review;
import org.example.springwebcatalog.Services.ServiceInterfaces.ProductService;
import org.example.springwebcatalog.Services.ServiceInterfaces.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable UUID id, Model model, Principal principal) {
        Product product = productService.getProductById(id);

        model.addAttribute("product", product);
        model.addAttribute("newReview", new Review());
        model.addAttribute("avgRating", productService.getAverageRating(product));

        addAttributes(model, product.getName(), principal, "product/productProfile");


        return "interface";
    }

    @PostMapping("/product/{id}/addReview")
    @Transactional
    public String addReview(@PathVariable UUID id, @ModelAttribute("newReview") Review newReview) {
        Product product = productService.getProductById(id);
        newReview.setProduct(product);
        product.addReview(newReview);
        reviewService.saveReview(newReview);

        return "redirect:/product/" + id;
    }


    @GetMapping("/")
    public String showMainPage(Model model, Principal principal) {
        List<Product> productList = productService.getAllProducts();

        addAttributes(model, "Main page", principal, "index");

        model.addAttribute("products", productList);

        return "interface";
    }

    @GetMapping("/search")
    @Transactional
    public String searchProducts(@RequestParam(name = "query", required = false) String query,
                                 @RequestParam(name = "sort", required = false, defaultValue = "name") String sortField,
                                 @RequestParam(name = "order", required = false, defaultValue = "asc") String sortOrder,
                                 Principal principal,
                                 Model model) {
        List<Product> searchResults;
        if (query != null && !query.isEmpty()) {
            searchResults = productService.sortProducts(productService.findProducts(query), sortField, sortOrder);
        } else {
            searchResults = productService.sortProducts(productService.getAllProducts(), sortField, sortOrder);
        }
        addAttributes(model, "Main page", principal, "index");
        model.addAttribute("searchResults", searchResults);

        return "interface";
    }

    private void addAttributes(Model model, String title, Principal principal, String customTemplate) {
        model.addAttribute("pageTitle", title);
        if (principal != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("loggedIn", false);
            model.addAttribute("username", "-");
        }
        model.addAttribute("contentTemplate", customTemplate);
    }

}
