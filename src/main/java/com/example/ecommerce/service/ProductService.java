package com.example.ecommerce.service;


import com.example.ecommerce.dto.request.CreateProductRequest;
import com.example.ecommerce.dto.request.UpdateProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.exception.InvalidFilterException;
import com.example.ecommerce.exception.InvalidPaginationException;
import com.example.ecommerce.exception.InvalidSortException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository  productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Helper method for sorting
    private void sortProducts(List<Product> products, String sort) {

        if (sort == null || sort.isBlank()) {
            return;
        }

        String[] sortParts = sort.split(",");

        String field = sortParts[0];
        String direction = sortParts.length > 1
                ? sortParts[1]
                : "asc";

        Comparator<Product> comparator;

        switch (field) {

            case "price":
                comparator = Comparator.comparing(Product::getPrice);
                break;

            case "name":
                comparator = Comparator.comparing(
                        Product::getName,
                        String.CASE_INSENSITIVE_ORDER);
                break;

            case "stockQuantity":
                comparator = Comparator.comparing(Product::getStockQuantity);
                break;

            default:
                throw new InvalidSortException(
                        "Invalid sort field: " + field);
        }

        if (direction.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        } else if (!direction.equalsIgnoreCase("asc")) {
            throw new InvalidSortException(
                    "Invalid sort direction: " + direction);
        }

        products.sort(comparator);
    }

    public List<ProductResponse> getAllProducts(int page, int size, String category, String brand, Double minPrice, Double maxPrice, String sort) {
        logger.info( "Fetching products. Page: {}, Size: {}, Category: {}, Brand: {}, MinPrice: {}, MaxPrice: {}, Sort: {}",
                page, size, category, brand, minPrice, maxPrice, sort);

        if (minPrice != null && minPrice < 0) {
            throw new InvalidFilterException("minPrice cannot be negative");
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new InvalidFilterException("maxPrice cannot be negative");
        }

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new InvalidFilterException(
                    "minPrice cannot be greater than maxPrice");
        }

        List<Product> products = new ArrayList<>(productRepository.findAll());

        // Apply category filter if provided
        if (category != null && !category.isBlank()) {
            products = products.stream()
                    .filter(product ->
                            product.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        // Apply brand filter if provided
        if (brand != null && !brand.isBlank()) {
            products = products.stream()
                    .filter(product ->
                            product.getBrand().equalsIgnoreCase(brand))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (minPrice != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() >= minPrice)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        if (maxPrice != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() <= maxPrice)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        sortProducts(products, sort);

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
