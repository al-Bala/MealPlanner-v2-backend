package com.mealplannerv2.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Product getProductByName(String name);
    Product getProductById(String id);
    List<Product> findByNameContainingIgnoreCase(String name);
}
