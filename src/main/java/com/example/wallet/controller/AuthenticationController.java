package com.example.wallet.controller;

import com.example.wallet.dto.*;
import com.example.wallet.service.AuthenticationService;
import com.example.wallet.service.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest
                                                                                      registerRequest){
        return ResponseEntity.status(201).body(authenticationService.registrationUser(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest
                                                                   authenticationRequest){
        return ResponseEntity.status(200).body(authenticationService.authentication(authenticationRequest));
    }

    @PostMapping("/parse")
    public ResponseEntity<?> getSomeSensitiveData(@RequestBody JsonParseRequestDto parseRequestDto) {

        try {
            JwtParseResponseDto jwtParseResponseDto = jwtService.parseJwt(parseRequestDto.getToken());
            return new ResponseEntity<>(jwtParseResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("JWT parsing error: {}, token: {}", e.getLocalizedMessage(), parseRequestDto);
            e.printStackTrace();

            return new ResponseEntity<>(new ErrorDto(e.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
