package com.jwt.store.service;


import com.jwt.store.model.AppUser;
import com.jwt.store.model.UserRole;
import com.jwt.store.repository.AppUserRepo;
import com.jwt.store.repository.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //lombok create constructor with all variables (appUserRepo, appUserRoleRepo)
@Transactional // to change entity in repo without saving it
@Slf4j // add log to service
public class AppUserServiceImplementation implements AppUserService, UserDetailsService {
    private final AppUserRepo appUserRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // need to override this method to implement UserDetailsService
        AppUser user = appUserRepo.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("user not founded"));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName()))); //Transform our user roles to GrantedAuthority
        return new User(user.getEmail(), user.getPassword(), authorities); //returning not AppUser, but User, who need to Spring Security
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to the database", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        appUserRepo.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("user not founded")).getRoles().add(userRoleRepo.findByName(roleName));
        // don't need to save because of @Transactional
    }

    @Override
    public AppUser getUser(String email) {
        return appUserRepo.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("user not founded"));
    }

    @Override
    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }
}
