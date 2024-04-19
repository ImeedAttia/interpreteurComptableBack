package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.Response.UserResponse;
import com.example.interpreteurcomptable.Entities.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getUser();
}
