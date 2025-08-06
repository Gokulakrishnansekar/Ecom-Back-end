package com.gokul.ecom_website.repository;

import com.gokul.ecom_website.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInterface extends JpaRepository<Product, Integer> {

    @Query("SELECT p from Product p where lower(p.name)  like  lower(concat('%',:name,'%'))")
    List<Product> searchProduct(String name);
}
