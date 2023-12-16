package org.example.springwebcatalog.Control;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.springwebcatalog.Model.Product.Product;
import org.example.springwebcatalog.Model.Product.Tag;
import org.example.springwebcatalog.Model.User.CustomUser;
import org.example.springwebcatalog.Services.ServiceInterfaces.ProductService;
import org.example.springwebcatalog.Services.ServiceInterfaces.TagService;
import org.example.springwebcatalog.Services.ServiceInterfaces.UserService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Controller
public class UserProfileController {

    private final UserService userService;
    private final ProductService productService;
    private final TagService tagService;

    public UserProfileController(UserService userService, ProductService productService, TagService tagService) {
        this.userService = userService;
        this.productService = productService;
        this.tagService = tagService;
    }

    @GetMapping("/profile")
    @Transactional
    public String userProfile(Model model, Principal principal) {

        CustomUser customUser = userService.findUserByName(principal.getName());
        List<Product> sellerProducts = productService.getProductByCustomUser(customUser);
        model.addAttribute("userProducts", sellerProducts);
        model.addAttribute("customUser", customUser);

        Hibernate.initialize(customUser.getPurchasedItems());
        model.addAttribute("purchased", customUser.getPurchasedItems());
        addTemplate(customUser, "user/profile", "Your profile", model);

        return "interface";
    }

    @GetMapping("/new")
    public String showAddProductForm(Model model, Principal principal) {
        Product product = new Product();

        model.addAttribute("product", product);
        addTemplate(userService.findUserByName(principal.getName()),
                "product/add",
                "Add product", model);

        return "interface";
    }


    @PostMapping("/new")
    public String saveProduct(@RequestParam("image") MultipartFile imageFile,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam double price,
                              @RequestParam int count,
                              @RequestParam String tags,
                              Principal principal
    ) throws IOException {
        Product product = new Product(name, description, price, count);

        CustomUser seller = userService.findUserByName(principal.getName());
        product.setSeller(seller);

        Set<Tag> tagSet = tagService.processTagsAndSave(tags);

        product.setTags(tagSet);

        if (!imageFile.isEmpty()) {
            byte[] imageBytes = imageFile.getBytes();
            product.setImage(imageBytes);
        }

        productService.saveProduct(product);

        return "redirect:/profile";
    }


    @PostMapping("/profile")
    public String saveChanges(@ModelAttribute("product") Product updatedProduct,
                              @RequestParam("imageFile") MultipartFile multipartFile,
                              @RequestParam("tagsAsString") String tags,
                              Principal principal) throws IOException {
        CustomUser seller = userService.findUserByName(principal.getName());

        if (!multipartFile.isEmpty()) {
            byte[] imageBytes = multipartFile.getBytes();
            updatedProduct.setImage(imageBytes);
        }

        Set<Tag> tagSet = tagService.processTagsAndSave(tags);

        updatedProduct.setSeller(seller);
        updatedProduct.setTags(tagSet);

        productService.saveProduct(updatedProduct);

        return "redirect:/profile";
    }

    @GetMapping("/profile/{id}")
    @Transactional
    public String editProduct(@PathVariable("id") UUID uuid,
                              Model model,
                              Principal principal) {
        Product product = productService.getProductById(uuid);
        model.addAttribute("tagsAsString", tagService.tagsToString(product.getTags()));
        model.addAttribute("editProduct", product);
        addTemplate(userService.findUserByName(principal.getName()),
                "product/edit",
                "Edit product", model);

        return "interface";
    }

    @PostMapping("/profile/delete/{id}")
    @Transactional
    public String deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "redirect:/profile";
    }


    private void addTemplate(CustomUser customUser, String contentTemplate, String title, Model model) {
        model.addAttribute("contentTemplate", contentTemplate);
        model.addAttribute("loggedIn", true);
        model.addAttribute("pageTitle", title);
        model.addAttribute("username", customUser.getUsername());
    }

}
