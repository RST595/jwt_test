package com.jwt.store.service;

import com.jwt.store.model.AppUser;
import com.jwt.store.model.UserRole;

import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUser user);
    UserRole saveRole(UserRole role);
    void addRoleToUser(String email, String name);
    AppUser getUser(String email);
    List<AppUser> getUsers(); //change to Page
}
