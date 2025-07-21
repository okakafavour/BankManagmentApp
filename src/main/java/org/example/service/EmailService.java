package org.example.service;

public interface EmailService {
    void sendVerificationEmail(String email, String link);
}
