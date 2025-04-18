package org.bringme.service;

public interface EmailService {

    void sendVerificationCode(String email);

    void validateCode(String userInput, String email);

    void sendEmail(String message, String subject, Long requesterUserId);
}
