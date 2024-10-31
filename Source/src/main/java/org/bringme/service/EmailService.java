package org.bringme.service;

public interface EmailService {

    String sendVerificationCode(String email);

    int validateCode(String userInput, String email);

    void sendEmail(String message, String subject, Long requesterUserId);
}
