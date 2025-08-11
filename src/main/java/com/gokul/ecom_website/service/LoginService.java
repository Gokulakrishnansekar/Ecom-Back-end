package com.gokul.ecom_website.service;

import com.gokul.ecom_website.Entity.RolesEntity;
import com.gokul.ecom_website.model.AuthResponse;
import com.gokul.ecom_website.model.MyUserPrinciple;
import com.gokul.ecom_website.model.PasswordModel;
import com.gokul.ecom_website.model.ResetPasswordModel;
import com.gokul.ecom_website.Entity.UsersModel;
import com.gokul.ecom_website.repository.UserDetailsRepo;
import com.gokul.ecom_website.repository.UserRolesRepo;
import com.gokul.ecom_website.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private UserRolesRepo userRolesRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;


    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    public ResponseEntity<AuthResponse> verify(UsersModel user) {

        user.setPassword(encoder.encode("admin"));
        userDetailsRepo.save(user);
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),user.getPassword()
        ));

        if(authentication.isAuthenticated())
        {
            MyUserPrinciple userDetails= (MyUserPrinciple) authentication.getPrincipal();


            AuthResponse token=new AuthResponse(jwtUtils.generateToken(userDetails.getUser()));

            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<AuthResponse  > verifyBySSO(String token) {

        try
        {
        String userName = jwtUtils.verifyGoogleToken(token);
        UsersModel user=userDetailsRepo.findByusername(userName);
        if(user==null)
        {
           user=new UsersModel();
            user.setMail(userName);
            user.setMail(userName);
            userDetailsRepo.save(user);
        }

            AuthResponse response= new AuthResponse(jwtUtils.generateToken(user));
            log.warn(response.getToken());
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();


        return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }


    public UsersModel verifyEmail(String email){

        if(userDetailsRepo.findByEmail(email)!=null)
        {
            return userDetailsRepo.findByEmail(email);
        }
        throw new RuntimeException("User not found");
    }

    public void changePassword(PasswordModel passwordModel){
            UsersModel user=userDetailsRepo.findByusername(passwordModel.getName());

           if(encoder.matches(passwordModel.getCurrentPassword(), user.getPassword()))
           {
               user.setPassword(encoder.encode(passwordModel.getNewPassword()));
               log.info("same password");
               userDetailsRepo.save(user);
           }
           else {
               throw new InvalidParameterException("password mismatch");
           }

    };


public void deleteById(Integer id){
    userDetailsRepo.deleteById(id);

}

    public List<UsersModel> getUsers(){

        return userDetailsRepo.findAll();
    }

    public UsersModel registerUser(UsersModel user){
        if(userDetailsRepo.findByusername(user.getUsername())!=null)
        {
            throw new RuntimeException("user name already exist");
        }
        if(userDetailsRepo.findByEmail(user.getMail())!=null)
        {
            throw new RuntimeException("Email already exist");
        }
        user.setPassword(encoder.encode("admin"));
        // Fetch roles by their names (assuming you pass role names)
        List<String> roleNames = user.getRoles().stream()
                .map(RolesEntity::getRole_name)
                .collect(Collectors.toList());

        List<RolesEntity> managedRoles = userRolesRepo.findByRolesNameIn(roleNames);

        // Set the managed roles to user
        user.setRoles(managedRoles);
        return userDetailsRepo.save(user);
    }

    public void generateTokenAndsendEmail(UsersModel user){
     String token=  jwtUtils.generateToken(user);
     try {
         emailService.sendEmailwithToken(user.getMail(),token);
     }
     catch (Exception e){
     }
    }

    public String validateTokenAndChangePassword(ResetPasswordModel resetPasswordModel)
    {
       if( this.jwtUtils.validateToken(resetPasswordModel.getToken()))
        {

            String name=this.jwtUtils.extractName(resetPasswordModel.getToken());
            changePassword(name,resetPasswordModel.getPassword());
        }
       else {

           throw new RuntimeException("token invalid");
       }
       return null;

    }
    public String changePassword(String name,String password){

       UsersModel user= userDetailsRepo.findByusername(name);
       user.setPassword(encoder.encode(password));
       userDetailsRepo.save(user);
       return null;
    }
}
