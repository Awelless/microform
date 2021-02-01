package com.softarex.microform.controller;

import com.softarex.microform.domain.User;
import com.softarex.microform.domain.Validation;
import com.softarex.microform.exception.UserNotUniqueException;
import com.softarex.microform.security.AuthenticationService;
import com.softarex.microform.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public User getMyProfile(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return null;
        }

        //@AuthenticationPrincipal can store old data
        return userService.getById(currentUser.getId());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Validated(Validation.Create.class) @RequestBody User user,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ControllerUtils.getErrorResponse(bindingResult);
        }

        try {
            user = userService.create(user);
            return ResponseEntity.ok(user);

        } catch (UserNotUniqueException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal User currentUser,
            @Validated(Validation.Edit.class) @RequestBody User newUser,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ControllerUtils.getErrorResponse(bindingResult);
        }

        return ResponseEntity.ok(userService.update(currentUser, newUser));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User currentUser,
            @RequestParam String oldPassword, @RequestParam String newPassword,
            HttpServletResponse response) {

        if (!userService.isPasswordCorrect(currentUser, oldPassword)) {
            return ControllerUtils.getErrorResponse(
                    "Invalid password", HttpStatus.UNAUTHORIZED);
        }

        User user;

        try {
            user = userService.updatePassword(currentUser, newPassword);
        } catch (IllegalArgumentException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            authenticationService.authenticate(user.getUsername(), newPassword, response);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AuthenticationException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNAUTHORIZED);
        }
    }
}
