package com.marcel.crud_springb_angular.jwt.controller;

import com.marcel.crud_springb_angular.dto.LoginRequest;
import com.marcel.crud_springb_angular.dto.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.marcel.crud_springb_angular.jwt.service.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        String token = jwtService.generateToken(authentication);

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        authentication.getName()
                )
        );
    }
}