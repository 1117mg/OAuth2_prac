package com.practice.oauth2_prac.product.repository;

import com.practice.oauth2_prac.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}