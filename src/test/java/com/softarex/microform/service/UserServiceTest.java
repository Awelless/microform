package com.softarex.microform.service;

import com.softarex.microform.domain.User;
import com.softarex.microform.exception.UserNotUniqueException;
import com.softarex.microform.repository.UserRepository;
import com.softarex.microform.util.MailSender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:src/test/resources/application.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MailSender mailSender;

    private User user;

    private static final String EMAIL        = "t@gmail.com";
    private static final String PASSWORD     = "1234";
    private static final String FIRST_NAME   = "Igor";
    private static final String LAST_NAME    = "Shmeltsov";
    private static final String PHONE_NUMBER = "375298886677";

    @Before
    public void init() {
        user = new User(
                1L, EMAIL, PASSWORD,
                FIRST_NAME, LAST_NAME, PHONE_NUMBER
        );

        when(userRepository.save(any())).then(returnsFirstArg());

        when(passwordEncoder.encode(anyString())).then(returnsFirstArg());

        doNothing().when(mailSender).sendActivationMessage(any());
        doNothing().when(mailSender).sendPasswordChangeMessage(any());
    }

    @Test
    public void testCreateShouldCreateWhenUnique() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(null);

        User result = userService.create(user);

        Assert.assertEquals(user, result);

        Mockito.verify(userRepository, Mockito.times(1)) .findByEmail(EMAIL);
        Mockito.verify(userRepository, Mockito.times(1)) .save(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(PASSWORD);
        Mockito.verify(mailSender, Mockito.times(1))     .sendActivationMessage(user);
    }

    @Test(expected = UserNotUniqueException.class)
    public void testCreateShouldThrowExceptionWhenNotUnique() {
        User createdUser = new User() {{
            setId(2L);
            setEmail(EMAIL);
        }};

        when(userRepository.findByEmail(EMAIL)).thenReturn(createdUser);

        userService.create(user);
    }

    @Test
    public void testUpdateShouldUpdateExcludingEmailAndPassword() {
        String newEmail       = "t@gm.coo";
        String newPassword    = "7654321";
        String newFirstName   = "Gleb";
        String newLastName    = "Shilo";
        String newPhoneNumber = "375990004442";

        User newUser = new User(null, newEmail, newPassword, newFirstName, newLastName, newPhoneNumber);

        User result = userService.update(user, newUser);

        Assert.assertEquals(user, result);

        Assert.assertEquals(EMAIL, result.getEmail());
        Assert.assertEquals(PASSWORD, result.getPassword());
        Assert.assertEquals(newFirstName, result.getFirstName());
        Assert.assertEquals(newLastName, result.getLastName());
        Assert.assertEquals(newPhoneNumber, result.getPhoneNumber());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void testIsPasswordCorrectShouldReturnTrueWhenCorrect() {
        when(passwordEncoder.matches(PASSWORD, PASSWORD)).thenReturn(true);

        boolean result = userService.isPasswordCorrect(user, PASSWORD);

        Assert.assertTrue(result);

        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(PASSWORD, PASSWORD);
    }

    @Test
    public void testIsPasswordCorrectShouldReturnFalseWhenIncorrect() {
        String testPassword = "123";

        when(passwordEncoder.matches(testPassword, PASSWORD)).thenReturn(false);

        boolean result = userService.isPasswordCorrect(user, testPassword);

        Assert.assertFalse(result);

        Mockito.verify(passwordEncoder, Mockito.times(1)).matches(testPassword, PASSWORD);
    }

    @Test
    public void testUpdatePasswordShouldUpdateWhenCorrectApplied() {
        String newPassword = "123456";

        User result = userService.updatePassword(user, newPassword);

        Assert.assertEquals(user, result);
        Assert.assertEquals(newPassword, result.getPassword());

        Mockito.verify(userRepository, Mockito.times(1)) .save(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(newPassword);
        Mockito.verify(mailSender, Mockito.times(1))     .sendPasswordChangeMessage(user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePasswordShouldThrowExceptionWhenIncorrectApplied() {
        String newPassword = "1234";

        userService.updatePassword(user, newPassword);
    }
}
