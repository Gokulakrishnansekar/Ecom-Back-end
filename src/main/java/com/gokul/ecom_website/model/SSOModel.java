package com.gokul.ecom_website.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SSOModel {
    private String token;

    public SSOModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
