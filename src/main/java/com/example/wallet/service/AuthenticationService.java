package com.example.wallet.service;


import com.example.wallet.dto.AuthenticationRequest;
import com.example.wallet.dto.AuthenticationResponse;
import com.example.wallet.dto.RegistrationRequest;
import com.example.wallet.entity.ROLE;
import com.example.wallet.entity.User;
import com.example.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final NewService newService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                                 AuthenticationManager authenticationManager, NewService newService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.newService = newService;
    }

    public AuthenticationResponse registrationUser(RegistrationRequest registerRequest) {

        User user = new User();

        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setMiddleName(registerRequest.getMiddleName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(ROLE.USER);

        userRepository.save(user);

        String message = "Your otp code";

        newService.sendSms(registerRequest.getPhoneNumber(), message);

        var jwtToken = jwtService.generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                jwtToken
        );

        return authenticationResponse;
    }

    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword()));

        var user = userRepository.findByPhoneNumber(authenticationRequest.getUsername());

        String message = "Your otp code";

        newService.sendSms(authenticationRequest.getUsername(), message);

        var jwtToken = jwtService.generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken);

        return authenticationResponse;
    }

}
