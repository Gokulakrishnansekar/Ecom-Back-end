package com.gokul.ecom_website.model;

import com.gokul.ecom_website.Entity.RolesEntity;
import com.gokul.ecom_website.Entity.UsersModel;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserPrinciple implements UserDetails {


   @Getter
   private UsersModel user;
   private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    public MyUserPrinciple(UsersModel user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<String> roles=user.getRoles().stream().map(RolesEntity::getRole_name).toList();
//        return Collections.singleton(roles) ;

        return user.getRoles().stream()
                .map(RolesEntity::getRole_name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
