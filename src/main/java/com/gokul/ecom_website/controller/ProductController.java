package com.gokul.ecom_website.controller;

import com.gokul.ecom_website.model.AuthResponse;
import com.gokul.ecom_website.model.MyUserPrinciple;
import com.gokul.ecom_website.model.Product;
import com.gokul.ecom_website.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.MethodNotAllowedException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")//allow origins for actual request
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService=productService;
    }

    @GetMapping("/")
    public String index(){
        return "welcome to the page";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){

        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }



    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String key){
        List<Product> products=this.productService.searchProduct(key);
        return new ResponseEntity<>(products,HttpStatus.OK);

    }
    @PutMapping("/product")
    public ResponseEntity<?> updateProduct(@RequestPart Product product,
                                           @RequestPart MultipartFile image, @AuthenticationPrincipal MyUserPrinciple principle) {
        try{
            if(!Objects.equals(product.getCreatedBy().getId(), principle.getUser().getId()))
            {
               throw new AccessDeniedException("product can be only deleted by creator");
            }
            if(product.getId()==null || product.getName().isEmpty()){
                return new ResponseEntity<>(new AuthResponse("Product name is mandatory"),HttpStatus.OK);
            }
            Product p=productService.addProduct(product,image);
            if(p==null)
            {
                throw new IllegalArgumentException("failed to update");
            }
            return new ResponseEntity<>(p,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        if(productService.getProductById(id)==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
        }
        return new ResponseEntity<>(productService.getProductById(id),HttpStatusCode.valueOf(200));
    }

    @PostMapping("/product")
    public ResponseEntity<?> addproduct(@RequestPart Product product,
                                        @RequestPart MultipartFile image) {
        try{

            if(product.getName()==null || product.getName().isEmpty()){
                throw new IllegalArgumentException("product name is mandatory");
            }

            Product np=productService.addProduct(product,image);
            return new ResponseEntity<>(np,HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id){
    try{
        if(productService.getProductById(id)==null){
            throw new NullPointerException("product is not available");
        }
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    catch(Exception e)
    {
        return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
    }

    }






}
