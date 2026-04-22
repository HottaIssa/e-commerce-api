package com.saihoz.e_commerce_api.user.dto;

import com.saihoz.e_commerce_api.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    String token;
    String username;
    String role;
//    String name;
//    String email;
//    String accessToken;
//    String refreshToken;
//    UserRole role;
//    Boolean isActive;
}
