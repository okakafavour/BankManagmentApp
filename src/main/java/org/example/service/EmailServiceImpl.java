package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String verificationLink) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("verify your BankApp account");
            helper.setText(
                    "<p>Hello,</p>" +
                            "<p>Please verify your email by clicking the link below:</p>" +
                            "<a href=\"" + verificationLink + "\">Verify Now</a>" +
                            "<p>This link will expire soon.</p>",
                            true
            );
            mailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(),e);
        }
    }
}
