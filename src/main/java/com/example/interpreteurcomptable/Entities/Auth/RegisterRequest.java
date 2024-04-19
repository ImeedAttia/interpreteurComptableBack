package com.example.interpreteurcomptable.Entities.Auth;

import com.example.interpreteurcomptable.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private boolean status;
    private String password;
    private Role role;
}
