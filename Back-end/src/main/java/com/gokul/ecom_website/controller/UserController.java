package com.gokul.ecom_website.controller;

import com.gokul.ecom_website.model.ResponseMessage;
import com.gokul.ecom_website.Entity.UsersModel;
import com.gokul.ecom_website.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {
    @Autowired
    LoginService loginService;
    @GetMapping("/users")
    public ResponseEntity<List<UsersModel>> getUsers(){
        return  new ResponseEntity<>( this.loginService.getUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Integer id){
     
         this.loginService.deleteById(id);
    }


    @PostMapping("/users/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody UsersModel user)
    {
        try{
            loginService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("User Created Successfully, please use Forget password to set password while login"));
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()),HttpStatus.OK);
        }
    }
}
