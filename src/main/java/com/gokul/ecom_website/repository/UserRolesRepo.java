package com.gokul.ecom_website.repository;

import com.gokul.ecom_website.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepo extends JpaRepository<RolesEntity,Integer> {
    @Query("select r from RolesEntity r where r.role_name in :names")
    List<RolesEntity> findByRolesNameIn(List<String>  names);
}
