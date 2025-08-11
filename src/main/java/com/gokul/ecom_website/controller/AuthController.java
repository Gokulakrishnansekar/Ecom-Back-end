package com.gokul.ecom_website.controller;

import com.gokul.ecom_website.Entity.UsersModel;
import com.gokul.ecom_website.model.*;
import com.gokul.ecom_website.service.AuthService;
import com.gokul.ecom_website.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LoginService loginService;


    @PostMapping("login")
    private ResponseEntity<AuthResponse> getusers(@RequestBody UsersModel user)
    {

        return loginService.verify(user);
    }

    @PostMapping("/login/sso")
    private ResponseEntity<AuthResponse> loginBySSO(@RequestBody SSOModel token)
    {

        return loginService.verifyBySSO(token.getToken());
    }

    @PostMapping("/login/forgot-password")
    public ResponseEntity<Fpass> forgotPassword(@RequestBody String email){

        try{
         UsersModel user= loginService.verifyEmail(email);
         loginService.generateTokenAndsendEmail(user);

            return new ResponseEntity<>(new Fpass("please Check the mail to reset password"),HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(new Fpass(e.getMessage()),HttpStatus.OK);
        }

    }

    @GetMapping("dummy")
    public String working(){
        List<UsersModel>user =loginService.getUsers();
        return "it is working fine as expected"+ user.toString();
    }



    @PostMapping("change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordModel passwordModel){
        try
        {

           loginService.changePassword(passwordModel);

                return new ResponseEntity<>(null,HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody ResetPasswordModel token){
            try{
                this.loginService.validateTokenAndChangePassword(token);
                return new ResponseEntity<>(new ResponseMessage("Password changed successfully"),HttpStatus.OK);

            }
            catch (Exception e)
            {
                return new ResponseEntity<>(new ResponseMessage(e.getMessage()),HttpStatus.NOT_ACCEPTABLE);
            }
    }


}
