package com.codexdei.crudjuegoahorcado.ahorcado.security.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.LoginRequestDto;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.Role;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.User;
import com.codexdei.crudjuegoahorcado.ahorcado.repositories.UserRepository;
import com.codexdei.crudjuegoahorcado.ahorcado.security.jwt.JwtService;
import com.codexdei.crudjuegoahorcado.ahorcado.security.dto.JwtResponseDto;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager,
            JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public JwtResponseDto authenticate(LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user '" + username + "' was not found"));

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        return new JwtResponseDto(token, user.getUsername(), user.getRoles());
    }

}
