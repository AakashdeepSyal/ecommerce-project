package com.example.ecommerce.repository;


import com.example.ecommerce.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

    private final List<Product> products = new ArrayList<>();
    private long nextId = 1l;  // temporary solution for getting id incremented for creating products as we are using right now in -memory array list.

    public List<Product> findAll() {
        return products;
    }

    public Product findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public Product save(Product product) {
        product.setId(nextId++);  // temporary solution for getting id incremented for creating products as we are using right now in -memory array list.
        products.add(product);
        return product;
    }

    public Product updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                return product;
            }
        }

        return null;
    }

    public void deleteById(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }
}
