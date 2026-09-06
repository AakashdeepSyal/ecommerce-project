package com.example.ecommerce.service;


import com.example.ecommerce.dto.request.CreateProductRequest;
import com.example.ecommerce.dto.request.UpdateProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.exception.InvalidPaginationException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository  productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts(int page, int size) {
        logger.debug("This is a DEBUG log");
        logger.info("Fetching all products");

        List<Product> products = productRepository.findAll();

        //handling one edge case if product size is 0
        if (products.isEmpty()) {
            return List.of();
        }

        int start = page * size;
        int end = Math.min(start + size, products.size());

        //handling the case where the page does not exist
        if (start >= products.size()) {
            throw new InvalidPaginationException("The Requested page does not exist");
        }

        List<Product> paginatedProducts = products.subList(start, end);
        return paginatedProducts.stream().map(ProductResponse::new).toList(); // converting the list of Products form repository into list of product response dto's
    }

    public ProductResponse getProductById(Long id) {
        logger.info("Fetching product with id {}", id);
        Product product =  productRepository.findById(id);

        // Exception Handling
        if (product == null) {
            logger.warn("Product with id {} not found", id);
            throw new ProductNotFoundException(
                    "Product with id " + id + " not found"
            );
        }
        return new ProductResponse(product);
    }

    public ProductResponse createProduct(CreateProductRequest requestProduct) {
        logger.info("Creating product with SKU {}", requestProduct.getSku());
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
        logger.info("Updating product with id {}", id);
        Product product = productRepository.findById(id);

        // Exception Handling
        if (product == null) {
            logger.warn("Product with id {} not found", id);
            throw new ProductNotFoundException(
                    "Product with id " + id + " not found"
            );
        }

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
        logger.info("Deleting product with id {}", id);
        Product product = productRepository.findById(id);

        // Exception Handling
        if (product == null) {
            logger.warn("Product with id {} not found", id);
            throw new ProductNotFoundException(
                    "Product with id " + id + " not found"
            );
        }
        productRepository.deleteById(id);
    }
}
