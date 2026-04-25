package com.saihoz.e_commerce_api.user;

import com.saihoz.e_commerce_api.user.dto.AuthResponseDTO;
import com.saihoz.e_commerce_api.user.dto.LoginRequestDTO;
import com.saihoz.e_commerce_api.user.dto.RegisterRequestDTO;
import com.saihoz.e_commerce_api.user.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User saveUser(RegisterRequestDTO request){
        User user = authMapper.fromDto(request);

        user.setRole(UserRole.ROLE_USER);
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO request){
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = repo.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDTO(token, user.getName(), user.getRole().name());
    }

}
