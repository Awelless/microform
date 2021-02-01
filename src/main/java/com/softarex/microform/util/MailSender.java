package com.softarex.microform.util;

import com.softarex.microform.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSender {
    @Value("${spring.mail.username}")
    private String emailFrom;

    private final JavaMailSender javaMailSender;

    public MailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void send(@NonNull final String emailTo,
            @NonNull final String subject, @NonNull final String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        new Thread(() -> javaMailSender.send(mailMessage)).start();
    }

    /**
     * Sends account creation notification to user
     * @param user The User to whom the message is sending
     */
    public void sendActivationMessage(@NonNull final User user) {
        String subject = "Account is created";
        String message = "Hello!\n You've just created account on MicroForm";

        send(user.getEmail(), subject, message);
    }

    /**
     * Sends password change notification to user
     * @param user The User to whom the message is sending
     */
    public void sendPasswordChangeMessage(@NonNull final User user) {
        String subject = "Password is changed";
        String message = "Hello!\n You've changed password on MicroForm";

        send(user.getEmail(), subject, message);
    }
}
