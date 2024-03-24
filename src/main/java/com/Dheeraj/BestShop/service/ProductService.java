package com.Dheeraj.BestShop.service;

import com.Dheeraj.BestShop.model.Product;
import com.Dheeraj.BestShop.respository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    public void save(Product product) {
        repository.save(product);
    }
}
