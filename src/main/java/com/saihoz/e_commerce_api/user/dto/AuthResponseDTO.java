package com.saihoz.e_commerce_api.user.dto;

import com.saihoz.e_commerce_api.user.UserRole;

public class AuthResponseDTO {
    String name;
    String email;
    String accessToken;
    String refreshToken;
    UserRole role;
    Boolean isActive;
}
