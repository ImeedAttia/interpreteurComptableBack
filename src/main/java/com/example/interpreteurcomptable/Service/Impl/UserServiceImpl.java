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

    @Override
    public UserResponse updateUser(long id,User user) {
        User u = userRepository.findById(id).orElseThrow();
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        User userRes =  userRepository.save(u);
        return UserResponse.builder()
                .id(userRes.getId())
                .firstName(userRes.getFirstName())
                .lastName(userRes.getLastName())
                .email(userRes.getEmail())
                .build();
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow();
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
