package org.bringme.service.impl;

import io.lettuce.core.RedisException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import org.bringme.exceptions.DataBaseException;
import org.bringme.exceptions.InvalidVerificationCodeException;
import org.bringme.exceptions.CannotSendingEmailException;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.service.EmailService;
import org.bringme.exceptions.CustomException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final PersonRepository personRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public EmailServiceImpl
            (JavaMailSender mailSender,
             PersonRepository personRepository,
             RedisTemplate<String, String> redisTemplate)
    {
        this.mailSender = mailSender;
        this.personRepository = personRepository;
        this.redisTemplate = redisTemplate;
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
            throw new CannotSendingEmailException(e, email, "verification code");
        }
        //save the 6-digits code in redis for 30 min
        try {
            redisTemplate.opsForValue().set("Verification-code:" +email, code, Duration.ofMinutes(30));
        } catch (RedisException e){
            throw new DataBaseException(e);
        }

    }

    /**
     * Validates the provided user input (verification code) against the code stored for the given email.
     * If the codes match, the user account is verified.
     *
     * @param userInput The verification code provided by the user.
     * @param email     The email address associated with the verification code.
     * @throws CustomException If the verification code is invalid, an exception with HTTP status
     *                         {@code 400 BAD_REQUEST} is thrown.
     */
    @Override
    public void validateCode(String userInput, String email) {
        // Get the code from Redis
        String originalCode =  redisTemplate.opsForValue().get("Verification-code:" +email);

        if (originalCode == null || !originalCode.equals(userInput)) {
            throw new InvalidVerificationCodeException(email);
        }
        try {
            Long userId = personRepository.getIdByEmailOrPhone(email);
            personRepository.verifyAccount(userId);
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        }
    }

    /**
     * Sends a custom email message to the user with the specified subject and message.
     * The email is sent to the user's email address associated with their requester user ID.
     *
     * @param message         The content of the email message.
     * @param subject         The subject of the email.
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
            throw new CannotSendingEmailException(e, person.get().getEmail(), "notifying");
        }
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
