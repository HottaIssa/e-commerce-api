package com.saihoz.e_commerce_api.user;

import com.saihoz.e_commerce_api.user.dto.LoginRequestDTO;
import com.saihoz.e_commerce_api.user.dto.RegisterRequestDTO;
import com.saihoz.e_commerce_api.user.mapper.AuthMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthMapper authMapper;

    @PostMapping("register")
    public User register(@Valid @RequestBody RegisterRequestDTO request){

        User user = authMapper.fromDto(request);

        user.setRole(UserRole.ADMIN);

        return service.saveUser(user);
    }

    @PostMapping("login")
    public String login(@Valid @RequestBody LoginRequestDTO request){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(request.getEmail());
        else
            return "Login Failed";

    }

}
