package org.bringme.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.bringme.repository.EmailRepository;
import org.bringme.repository.PersonRepository;
import org.bringme.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private final PersonRepository personRepository;
    public EmailServiceImpl(JavaMailSender mailSender, EmailRepository emailRepository, PersonRepository personRepository){
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
        this.personRepository = personRepository;
    }
    @Override
    @Async
    public String sendVerificationCode(String email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        String code = generateCode();
        try{
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("stmsgms@gmail.com", "no-reply@Bring-Me");
            helper.setTo(email);
            helper.setSubject("Verification code");
            helper.setText("Thank you for using \"Bring-Me\"\nYour verification code is: "+code+".\n", true);
            mailSender.send(mimeMessage);
        }catch (MessagingException | UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        emailRepository.saveCode(email, code);
        return code;
    }

    @Override
    public int validateCode(String userInput, String email) {
        String originalCode = emailRepository.getCode(email);
        if(!originalCode.equals(userInput)){
            return 1;
        }
        Long userId = personRepository.getIdByEmailOrPhone(email);
        personRepository.verifyAccount(userId);
        return 0;
    }

    private String generateCode(){
        Random random = new Random();
        StringBuilder code = new StringBuilder(6);
        for(int i = 0; i < 6;i++){
            int digit = random.nextInt(9) + 1;
            code.append(digit);
        }
        return code.toString();
    }
}
