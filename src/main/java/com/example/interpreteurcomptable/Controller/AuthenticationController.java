package com.example.interpreteurcomptable.Controller;


import com.example.interpreteurcomptable.Entities.Auth.AuthenticationRequest;
import com.example.interpreteurcomptable.Entities.Auth.AuthenticationResponse;
import com.example.interpreteurcomptable.Entities.Auth.RegisterRequest;
import com.example.interpreteurcomptable.Entities.Response.UserResponse;
import com.example.interpreteurcomptable.Entities.User;
import com.example.interpreteurcomptable.Service.Impl.AuthenticationService;
import com.example.interpreteurcomptable.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userClientService;

    // Singup function
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    //Login function
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

}
