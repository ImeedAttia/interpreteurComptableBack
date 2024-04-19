package com.example.interpreteurcomptable.Service.Impl;


import com.example.interpreteurcomptable.Entities.Company;
import com.example.interpreteurcomptable.Entities.Response.UserResponse;
import com.example.interpreteurcomptable.Entities.User;
import com.example.interpreteurcomptable.Repository.CompanyRepository;
import com.example.interpreteurcomptable.Repository.UserRepository;
import com.example.interpreteurcomptable.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    @Override
    public List<UserResponse> getUser() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        Company c = companyRepository.findByUserId(user.getId());
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .company(c)
                .build();
    }
}
