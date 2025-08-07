package com.gokul.ecom_website.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name="Users")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class UsersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private  String password;

    @Column(name="mail",unique = true)
    private String mail;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
            joinColumns=@JoinColumn(name="USER_ID"),
            inverseJoinColumns = @JoinColumn(name="ROLE_ID")

    )
    private List<RolesEntity> roles;


}
