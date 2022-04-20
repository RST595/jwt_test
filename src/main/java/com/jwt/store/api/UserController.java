package com.jwt.store.api;

import com.jwt.store.dto.RoleToUserRequestDTO;
import com.jwt.store.model.AppUser;
import com.jwt.store.model.UserRole;
import com.jwt.store.service.AppUserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AppUserServiceImplementation appUserService;


    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok().body(appUserService.getUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<AppUser> saveUsers(@RequestBody AppUser appUser){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString()); // ServletUriComponentsBuilder.fromCurrentContextPath() = localhost:8080
        return ResponseEntity.created(uri).body(appUserService.saveUser(appUser));
    }

    @PostMapping("/role/save")
    public ResponseEntity<UserRole> saveRole(@RequestBody UserRole userRole){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/role/save").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveRole(userRole));
    }

    @PostMapping("/addRole")
    public ResponseEntity<?> saveRole(@RequestBody RoleToUserRequestDTO form){
        appUserService.addRoleToUser(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build();
    }


}
