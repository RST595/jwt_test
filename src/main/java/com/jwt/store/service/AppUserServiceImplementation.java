package com.jwt.store.service;

import com.jwt.store.model.AppUser;
import com.jwt.store.model.UserRole;
import com.jwt.store.repository.AppUserRepo;
import com.jwt.store.repository.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor //lombok create constructor with all variables (appUserRepo, appUserRoleRepo)
@Transactional // to change entity in repo without saving it
@Slf4j // add log to service
public class AppUserServiceImplementation implements AppUserService{
    private final AppUserRepo appUserRepo;
    private final UserRoleRepo userRoleRepo;

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to the database", user.getEmail());
        return appUserRepo.save(user);
    }

    @Override
    public UserRole saveRole(UserRole role) {
        log.info("Saving new role {} to the database", role.getName());
        return userRoleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Add new role {} to the user {}", roleName, email);
        appUserRepo.findByEmail(email).getRoles().add(userRoleRepo.findByName(roleName));
        // don't need to save because of @Transactional
    }

    @Override
    public AppUser getUser(String email) {
        return appUserRepo.findByEmail(email);
    }

    @Override
    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }
}
