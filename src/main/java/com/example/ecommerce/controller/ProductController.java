package com.example.ecommerce.controller;


import com.example.ecommerce.dto.request.CreateProductRequest;
import com.example.ecommerce.dto.request.UpdateProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.exception.InvalidPaginationException;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        if(page < 0 || size <= 0) {
            throw new InvalidPaginationException("Page must be greater than or equal to 0 and size must be greater than 0");
        }
        List<ProductResponse> productResponseList = productService.getAllProducts(page, size);
        return ResponseEntity.ok(productResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest requestProduct) {
        ProductResponse productResponse = productService.createProduct(requestProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest requestProduct) {
        ProductResponse productResponse = productService.updateProduct(id, requestProduct);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


    // Test API for testing general exception handling
    @GetMapping("/test-error")
    public String testError() {
        throw new RuntimeException("This is a test exception");
    }

}
