package com.user_service.service;

import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import com.user_service.repo.UserRepository;
import com.user_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private UserServiceImpl userService;

    private User user;
    @BeforeEach
    void setup(){

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setPassword("encodedPassword");
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findUserByEmail_Success() throws Exception {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user);

        User userByEmail = userService.findUserByEmail("john.doe@example.com");
        assertNotNull(userByEmail);
        assertEquals(userByEmail.getEmail(), "john.doe@example.com");
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void findUserByEmail_Failure() throws Exception {

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        Exception exception = assertThrows(Exception.class,
                () -> userService.findUserByEmail("notfound@example.com"));

        assertTrue(exception.getMessage().contains("user not found with email"));
        verify(userRepository, times(1)).findByEmail("notfound@example.com");

    }

    @Test
    void saveUser_shouldCallRepositorySave() {
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }
}
