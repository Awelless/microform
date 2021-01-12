package com.softarex.internship.util;

import com.softarex.internship.domain.User;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MailSender {
    private final JavaMailSender javaMailSender;

    private void send(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        new Thread(() -> javaMailSender.send(mailMessage)).start();
    }

    public void sendActivationMessage(@NonNull final User user) {
        String subject = "Account is created";
        String message = String.format(
                "Hello, %s!/n" +
                "You've created account on MicroForm",
                user.getUsername()
        );

        send(user.getEmail(), subject, message);
    }

    public void sendPasswordChangeMessage(@NonNull final User user) {
        String subject = "Password is changed";
        String message = String.format(
                "Hello, %s!/n" +
                "You've changed password on MicroForm",
                user.getUsername()
        );

        send(user.getEmail(), subject, message);
    }
}
