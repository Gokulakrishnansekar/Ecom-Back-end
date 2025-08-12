package com.gokul.ecom_website.repository;

import com.gokul.ecom_website.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<RolesEntity,Integer> {

}
