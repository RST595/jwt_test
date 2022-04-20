package com.jwt.store.dto;


import lombok.Data;

@Data
public class RoleToUserRequestDTO {
    private String email;
    private String roleName;
}
