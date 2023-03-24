package com.example.wallet.service;


import com.example.wallet.dto.AuthenticationRequest;
import com.example.wallet.dto.AuthenticationResponse;
import com.example.wallet.dto.RegistrationRequest;
import com.example.wallet.entity.ROLE;
import com.example.wallet.entity.User;
import com.example.wallet.events.CreateEvents;
import com.example.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    private ApplicationEventPublisher eventPublisher;

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

        var jwtToken = jwtService.generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                jwtToken
        );

        CreateEvents publisher = new CreateEvents(
                this,
                "Add in DB and CREATE New User!"
        );
        eventPublisher.publishEvent(publisher);

        return authenticationResponse;
    }

    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword()));

        var user = userRepository.findByPhoneNumber(authenticationRequest.getUsername());

        var jwtToken = jwtService.generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken);

        return authenticationResponse;
    }

    public String getOtpCode(String phoneNumber) {

        String message = "Your otp code";

        newService.sendSms(phoneNumber, message);

        return "Your verificate otp code!";
    }
}
