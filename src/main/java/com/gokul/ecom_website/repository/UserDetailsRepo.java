package com.gokul.ecom_website.repository;

import com.gokul.ecom_website.Entity.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface
UserDetailsRepo extends JpaRepository<UsersModel,Integer> {
   @Query("SELECT u from UsersModel u where lower(u.username) = lower(:userName)")
     UsersModel findByusername(String userName);

   @Query("SELECT u from UsersModel u where lower(u.mail) = lower(:mail)")
    UsersModel findByEmail(@Param("mail") String mail);

}
