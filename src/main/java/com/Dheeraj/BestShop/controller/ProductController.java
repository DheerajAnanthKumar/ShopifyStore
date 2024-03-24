package com.Dheeraj.BestShop.controller;

import com.Dheeraj.BestShop.model.Product;
import com.Dheeraj.BestShop.model.ProductDto;
import com.Dheeraj.BestShop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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

    @GetMapping("/create")
    public String showCreatePage(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto",productDto);
        return "products/createProduct";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ){
        if(productDto.getImageFile().isEmpty()){
            result.addError(new FieldError("productDto","imageFile","The image file " +
                    "is required"));
        }
        if(result.hasErrors()){
            return "products/createProduct";
        }
        MultipartFile image = productDto.getImageFile();
        Date createAt = new Date();
        String storageFileName = createAt.getTime()+"_"+image.getOriginalFilename();
        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream,Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }catch(Exception ex){
            System.out.println("Exception:"+ex.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreateAt(createAt);
        product.setImageFileName(storageFileName);
        service.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(
            Model model, @RequestParam int id
    ){
        try{
            Product product = service.findById(id).get();
            model.addAttribute("product",product);
            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto",productDto);
        }catch (Exception ex){
            System.out.println("Exception: "+ex.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ){
        try{
            Product product = service.findById(id).get();
            model.addAttribute("product",product);
            if(result.hasErrors()){
                return "products/EditProduct";
            }
            if(!productDto.getImageFile().isEmpty()){
                String uploadDir ="public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                try{
                    Files.delete(oldImagePath);
                }catch (Exception ex){
                    System.out.println("Exception: "+ex.getMessage());
                }
                MultipartFile image = productDto.getImageFile();
                Date createAt = new Date();
                String storageFileName = createAt.getTime()+"_"+image.getOriginalFilename();
                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream,Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());

            service.save(product);

        }catch(Exception ex){
            System.out.println("Exception: "+ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(
            @RequestParam int id
    ){
        try{
            Product product=service.findById(id).get();

            Path imagePath = Paths.get("public/images/"+product.getImageFileName());
            try{
                Files.delete(imagePath);
            }catch (Exception ex){
                System.out.println("Exception: "+ex.getMessage());
            }
            service.delete(product);
        }catch(Exception ex){
            System.out.println("Exception: "+ex.getMessage());
        }
        return "redirect:/products";
    }
}
