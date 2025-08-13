package com.gokul.ecom_website.service;

import com.gokul.ecom_website.model.MyUserPrinciple;
import com.gokul.ecom_website.Entity.UsersModel;
import com.gokul.ecom_website.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersModel user= userDetailsRepo.findByusername(username);
       if(user==null)
        {
            throw new UsernameNotFoundException("User name not in the database");
        }
        return new MyUserPrinciple(user);
    }
}
