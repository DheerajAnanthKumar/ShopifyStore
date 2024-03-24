package com.Dheeraj.BestShop.controller;

import com.Dheeraj.BestShop.model.Product;
import com.Dheeraj.BestShop.model.ProductDto;
import com.Dheeraj.BestShop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping({"","/"})
    public String showProductList(Model model){
        List<Product> products = service.findAll();
        model.addAttribute("products",products);
        return "products/index";
    }

    @PostMapping("/create")
    public String showCreatePage(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/createProduct";
    }
}
