package com.gokul.ecom_website.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "ROLES")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Integer id;

    @JsonValue
    @Column(name = "ROLE_NAME")
    private String role_name;

    public RolesEntity(String name){
        this.role_name=name;
    }

    @ManyToMany(mappedBy = "roles")
    private Set<UsersModel> users=new HashSet<>();


}
