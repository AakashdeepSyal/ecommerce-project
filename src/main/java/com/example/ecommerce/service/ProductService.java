package com.example.ecommerce.service;


import com.example.ecommerce.dto.request.CreateProductRequest;
import com.example.ecommerce.dto.request.UpdateProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository  productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(ProductResponse::new).toList(); // converting the list of Products form repository into list of product response dto's
    }

    public ProductResponse getProductById(Long id) {
        return new ProductResponse(productRepository.findById(id));
    }

    public ProductResponse createProduct(CreateProductRequest requestProduct) {
        Product product = new Product();

        product.setName(requestProduct.getName());
        product.setDescription(requestProduct.getDescription());
        product.setSku(requestProduct.getSku());
        product.setCategory(requestProduct.getCategory());
        product.setBrand(requestProduct.getBrand());
        product.setPrice(requestProduct.getPrice());
        product.setDiscountedPercentage(requestProduct.getDiscountedPercentage());
        product.setStockQuantity(requestProduct.getStockQuantity());
        product.setCurrency(requestProduct.getCurrency());
        product.setImageUrl(requestProduct.getImageUrl());

        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id , UpdateProductRequest requestProduct) {
        Product product = productRepository.findById(id);
        product.setName(requestProduct.getName());
        product.setDescription(requestProduct.getDescription());
        product.setCategory(requestProduct.getCategory());
        product.setBrand(requestProduct.getBrand());
        product.setPrice(requestProduct.getPrice());
        product.setDiscountedPercentage(requestProduct.getDiscountedPercentage());
        product.setStockQuantity(requestProduct.getStockQuantity());
        product.setCurrency(requestProduct.getCurrency());
        product.setImageUrl(requestProduct.getImageUrl());

        Product updatedProduct = productRepository.updateProduct(product);
        return new ProductResponse(updatedProduct);
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
