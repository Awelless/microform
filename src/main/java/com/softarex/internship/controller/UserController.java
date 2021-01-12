package com.softarex.internship.controller;

import com.softarex.internship.domain.User;
import com.softarex.internship.security.AuthenticationService;
import com.softarex.internship.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public User getMyProfile(@AuthenticationPrincipal User currentUser) {
        //@AuthenticationPrincipal can store old data
        return userService.getById(currentUser.getId());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateProfile(
            @AuthenticationPrincipal User currentUser,
            @RequestBody User newUser
    ) {
        return userService.update(currentUser, newUser);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal User currentUser,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpServletResponse response
    ) {
        if (!userService.isPasswordCorrect(currentUser, oldPassword)) {
            return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.updatePassword(currentUser, newPassword);

        String token = authenticationService.authenticate(user);

        Cookie authCookie = new Cookie("Authorization", token);
        response.addCookie(authCookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
