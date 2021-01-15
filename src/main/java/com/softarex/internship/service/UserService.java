package com.softarex.internship.service;

import com.softarex.internship.domain.User;
import com.softarex.internship.exception.UserNotUniqueException;
import com.softarex.internship.repository.UserRepository;
import com.softarex.internship.util.MailSender;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public User getById(@NonNull final Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Checks user uniqueness and creates new User
     * @param user The User who is created
     * @return Created user from DB
     */
    public User create(@NonNull final User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserNotUniqueException("Email is already taken");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //Sending e-mail notification
        mailSender.sendActivationMessage(user);

        return userRepository.save(user);
    }

    /**
     * Updates all properties of user excluding password
     * @param user The User who is updated
     * @param newUser new version of the user
     * @return Updated user from DB
     */
    public User update(@NonNull User user, @NonNull final User newUser) {
        BeanUtils.copyProperties(newUser, user, "id", "password");
        return userRepository.save(user);
    }

    public boolean isPasswordCorrect(@NonNull final User user, @NonNull final String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Validates newPassword and updates user
     * @param user The User who is updated
     * @param newPassword new password of the user
     * @return Updated user from DB
     */
    public User updatePassword(@NonNull User user, @NonNull final String newPassword) {
        //Validating newPassword
        if (StringUtils.isEmpty(newPassword)) {
            throw new IllegalArgumentException("Password shouldn't be blank");
        }
        if (newPassword.length() < 6 || newPassword.length() > 255) {
            throw new IllegalArgumentException("Password length should be from 6 to 255");
        }
        if (!newPassword.matches("\\w+")) {
            throw new IllegalArgumentException("Password should contain only letters of latin alphabet, numbers and '_' symbol");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        //Sending e-mail notification
        mailSender.sendPasswordChangeMessage(user);

        return userRepository.save(user);
    }
}
