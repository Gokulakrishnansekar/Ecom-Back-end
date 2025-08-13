package com.gokul.ecom_website.service;


import com.gokul.ecom_website.Entity.UsersModel;
import com.gokul.ecom_website.controller.ProductController;
import com.gokul.ecom_website.model.MyUserPrinciple;
import com.gokul.ecom_website.model.Product;
import com.gokul.ecom_website.repository.ProductInterface;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
@Service
public class ProductService {
    //@Autowired
   ProductInterface prepo;

   public ProductService(ProductInterface prepo) {
       this.prepo=prepo;
   }


    public List<Product> getAllProducts(){
       List<Product> p=this.prepo.findAll();
       return p;
    }

    public Product getProductById(int id){
       return prepo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile image) throws IOException{
       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        MyUserPrinciple userDetails=(MyUserPrinciple) authentication.getPrincipal();
        Integer currentUserId=userDetails.getUser().getId();
        UsersModel user=new UsersModel();
        user.setId(currentUserId);
        product.setCreatedBy(user);

       product.setImageName(image.getOriginalFilename());
       product.setImageType(image.getContentType());
       product.setImageData(image.getBytes());
       prepo.save(product);
       return product;
    }

    public List<Product> searchProduct(String keyword){

       return prepo.searchProduct(keyword);
    }





    public void deleteProductById(int id) {
        prepo.deleteById(id);
    }
}
