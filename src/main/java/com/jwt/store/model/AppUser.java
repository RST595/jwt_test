package com.jwt.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) //EAGER - download roles together with user, LAZY - only when looking for user roles
    private Collection<UserRole> roles = new ArrayList<>();
}
