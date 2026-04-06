package com.saihoz.e_commerce_api.user.mapper;

import com.saihoz.e_commerce_api.user.User;
import com.saihoz.e_commerce_api.user.dto.RegisterRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User fromDto(RegisterRequestDTO registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return user;
    }

}
