package org.bringme.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.bringme.model.Person;
import org.bringme.repository.EmailRepository;
import org.bringme.repository.PersonRepository;
import org.bringme.service.EmailService;
import org.bringme.service.exceptions.CustomException;
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
        // TODO: Set the code for temporary period
        emailRepository.saveCode(email, code);
    }

    @Override
    public void validateCode(String userInput, String email) {
        String originalCode = emailRepository.getCode(email);
        if (!originalCode.equals(userInput)) {
            throw new CustomException("Invalid code", HttpStatus.BAD_REQUEST);
        }
        Long userId = personRepository.getIdByEmailOrPhone(email);
        personRepository.verifyAccount(userId);
    }

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
