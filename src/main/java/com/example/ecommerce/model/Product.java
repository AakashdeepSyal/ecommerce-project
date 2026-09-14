package com.example.ecommerce.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private String sku;
    private String category;
    private String brand;
    private double price;
    private double discountedPercentage;
    private int stockQuantity;
    private String currency;
    private String imageUrl;

    public Product() {
    }

    public Product( String name, String description, String sku, String category, String brand, double price, double discountedPercentage, int stockQuantity, String currency, String imageUrl) {
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.discountedPercentage = discountedPercentage;
        this.stockQuantity = stockQuantity;
        this.currency = currency;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountedPercentage() {
        return discountedPercentage;
    }

    public void setDiscountedPercentage(double discountedPercentage) {
        this.discountedPercentage = discountedPercentage;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
