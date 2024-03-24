package com.Dheeraj.BestShop.service;

import com.Dheeraj.BestShop.model.Product;
import com.Dheeraj.BestShop.respository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    public void delete(Product product) {
        repository.delete(product);
    }
}
