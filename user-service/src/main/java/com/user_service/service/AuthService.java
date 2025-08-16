package com.user_service.service;

import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import com.user_service.request.LoginRequest;
import com.user_service.request.SignupRequest;
import com.user_service.response.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface AuthService {
    User createUser(SignupRequest request) throws Exception;

    void sendLoginOtp(String email, USER_ROLE role) throws Exception;

    UserResponse signIn(LoginRequest request) throws Exception;

}