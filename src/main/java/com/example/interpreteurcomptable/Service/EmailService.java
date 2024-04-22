package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.User;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void sendEmailWithTemplate(User user);
}
