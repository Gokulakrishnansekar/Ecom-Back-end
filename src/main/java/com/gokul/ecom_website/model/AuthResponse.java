package com.gokul.ecom_website.model;

public class AuthResponse {
     String token = "";

    public String getToken() {
        return token;
    }

    public AuthResponse(String token)
     {
         this.token=token;
     }
}
