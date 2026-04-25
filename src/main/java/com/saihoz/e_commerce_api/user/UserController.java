package com.saihoz.e_commerce_api.user;

import com.saihoz.e_commerce_api.user.dto.AuthResponseDTO;
import com.saihoz.e_commerce_api.user.dto.LoginRequestDTO;
import com.saihoz.e_commerce_api.user.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("register")
    public User register(@Valid @RequestBody RegisterRequestDTO request){

        return service.saveUser(request);
    }

    @PostMapping("login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request){
        return service.login(request);
    }

}
