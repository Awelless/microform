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

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public User getById(@NonNull final Long id) {
        return userRepository.getOne(id);
    }

    public User create(@NonNull final User user) {
        if (userRepository.getByUsername(user.getUsername()) != null) {
            throw new UserNotUniqueException("Username is already taken");
        }
        if (userRepository.getByEmail(user.getEmail()) != null) {
            throw new UserNotUniqueException("Email is already taken");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        mailSender.sendActivationMessage(user);

        return userRepository.save(user);
    }

    public User update(@NonNull User oldUser, @NonNull final User newUser) {
        BeanUtils.copyProperties(newUser, oldUser, "id", "username", "password");
        return userRepository.save(oldUser);
    }

    public boolean isPasswordCorrect(@NonNull final User user, @NonNull final String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User updatePassword(@NonNull User user, @NonNull final String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        mailSender.sendPasswordChangeMessage(user);

        return userRepository.save(user);
    }
}
