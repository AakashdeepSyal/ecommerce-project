package com.example.ecommerce.repository;


import com.example.ecommerce.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    private final List<Product> products = new ArrayList<>();

    public List<Product> findAll() {
        return products;
    }

    public Product findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public Product save(Product product) {
        products.add(product);
        return product;
    }

    public Product updateProductById( Long id, Product product) {
        Product realProduct = products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (realProduct != null) {
            realProduct.setName(product.getName());
            realProduct.setPrice(product.getPrice());
            realProduct.setCategory(product.getCategory());
        }
        return realProduct;
    }

    public void deleteById(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}
