package com.codexdei.crudjuegoahorcado.ahorcado.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codexdei.crudjuegoahorcado.ahorcado.security.dto.JwtResponseDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.UserResponseDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.request.RegisterRequestDto;
import com.codexdei.crudjuegoahorcado.ahorcado.dto.request.response.response.LoginRequestDto;
import com.codexdei.crudjuegoahorcado.ahorcado.entities.User;
import com.codexdei.crudjuegoahorcado.ahorcado.security.service.AuthenticationService;
import com.codexdei.crudjuegoahorcado.ahorcado.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService,
            UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {

        JwtResponseDto response = authenticationService.authenticate(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @RequestBody RegisterRequestDto request) {

        // Construye el usuario a partir de los datos recibidos.
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setAdmin(request.isAdmin());

        // Permite que UserService asigne los roles y cifre la password.
        User savedUser = userService.save(user);

        // Convertimos la entidad a un DTO seguro para la respuesta.
        UserResponseDto response = new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRoles(),
                savedUser.isEnabled());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
