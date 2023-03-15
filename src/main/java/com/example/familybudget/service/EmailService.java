package com.example.familybudget.service;

public interface EmailService {
    void send(String emailTo, String subject, String message);
}
