package com.java.ne_starter.services.interfaces;

public interface EmailService {
    void sendVerificationEmail(String to, String token);
}