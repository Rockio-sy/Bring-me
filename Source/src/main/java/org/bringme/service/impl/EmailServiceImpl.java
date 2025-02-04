package org.bringme.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.bringme.model.Person;
import org.bringme.repository.EmailRepository;
import org.bringme.repository.PersonRepository;
import org.bringme.service.EmailService;
import org.bringme.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final PersonRepository personRepository;

    public EmailServiceImpl(JavaMailSender mailSender, EmailRepository emailRepository, PersonRepository personRepository) {
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
        this.personRepository = personRepository;
    }

    /**
     * Sends a verification code to the provided email address asynchronously.
     * The verification code is generated, sent via email, and saved in the database for future validation.
     *
     * @param email The email address where the verification code will be sent.
     * @throws CustomException If the email cannot be sent, an exception with HTTP status
     *                         {@code 500 INTERNAL_SERVER_ERROR} is thrown.
     */
    @Override
    @Async
    public void sendVerificationCode(String email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        String code = generateCode();
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("stmsgms@gmail.com", "no-reply@Bring-Me");
            helper.setTo(email);
            helper.setSubject("Verification code");
            helper.setText("Thank you for using \"Bring-Me\"\nYour verification code is: " + code + ".\n", true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException("Email is not sent", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        emailRepository.saveCode(email, code);
    }

    /**
     * Validates the provided user input (verification code) against the code stored for the given email.
     * If the codes match, the user account is verified.
     *
     * @param userInput The verification code provided by the user.
     * @param email The email address associated with the verification code.
     * @throws CustomException If the verification code is invalid, an exception with HTTP status
     *                         {@code 400 BAD_REQUEST} is thrown.
     */
    @Override
    public void validateCode(String userInput, String email) {
        String originalCode = emailRepository.getCode(email);
        if (!originalCode.equals(userInput)) {
            throw new CustomException("Invalid code", HttpStatus.BAD_REQUEST);
        }
        Long userId = personRepository.getIdByEmailOrPhone(email);
        personRepository.verifyAccount(userId);
    }

    /**
     * Sends a custom email message to the user with the specified subject and message.
     * The email is sent to the user's email address associated with their requester user ID.
     *
     * @param message The content of the email message.
     * @param subject The subject of the email.
     * @param requesterUserId The ID of the user to whom the email will be sent.
     * @throws CustomException If the email cannot be sent, an exception with HTTP status
     *                         {@code 409 CONFLICT} is thrown.
     */
    @Override
    public void sendEmail(String message, String subject, Long requesterUserId) {
        Optional<Person> person = personRepository.getById(requesterUserId);
        if (person.isEmpty()) {
            return;
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("stmsgms@gmail.com", "no-reply@Bring-Me");
            helper.setTo(person.get().getEmail());
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException("Cannot send then email: \n"+e.getMessage(), HttpStatus.CONFLICT);
        }
        System.out.println("Email Sent to:"+person.get().getFirstName());
    }

    /**
     * Generates a 6-digit random verification code consisting of digits 1-9.
     * This code is used for email verification purposes.
     *
     * @return {@link String} A 6-digit verification code.
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(9) + 1;
            code.append(digit);
        }
        return code.toString();
    }
}
