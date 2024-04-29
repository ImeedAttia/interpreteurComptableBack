package com.example.interpreteurcomptable.Service.Impl;


import com.example.interpreteurcomptable.Entities.Auth.AuthenticationRequest;
import com.example.interpreteurcomptable.Entities.Auth.AuthenticationResponse;
import com.example.interpreteurcomptable.Entities.Auth.RegisterRequest;
import com.example.interpreteurcomptable.Entities.Role;
import com.example.interpreteurcomptable.Entities.User;
import com.example.interpreteurcomptable.Repository.UserRepository;
import com.example.interpreteurcomptable.Service.EmailService;
import com.example.interpreteurcomptable.Service.UserService;
import com.example.interpreteurcomptable.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .status(true)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        User u = utilisateurRepository.save(user);
        //emailService.sendEmailWithTemplate(u);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword())
        );
        var user = utilisateurRepository.findByEmail(authenticationRequest.getEmail()).get();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }
}