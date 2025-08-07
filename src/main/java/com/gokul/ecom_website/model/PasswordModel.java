package com.gokul.ecom_website.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordModel {
    String name;
    String currentPassword;
    String newPassword;
}
