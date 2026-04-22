package com.saihoz.e_commerce_api.user;

import com.saihoz.e_commerce_api.user.dto.AuthResponseDTO;
import com.saihoz.e_commerce_api.user.dto.LoginRequestDTO;
import com.saihoz.e_commerce_api.user.dto.RegisterRequestDTO;
import com.saihoz.e_commerce_api.user.mapper.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    private AuthMapper authMapper;

    @PostMapping("register")
    public User register(@Valid @RequestBody RegisterRequestDTO request){

        User user = authMapper.fromDto(request);

        user.setRole(UserRole.ROLE_USER);

        return service.saveUser(user);
    }

    @PostMapping("login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request){

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, user.getName(), user.getRole().name());

    }

}
