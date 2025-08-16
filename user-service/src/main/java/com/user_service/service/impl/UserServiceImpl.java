package com.user_service.service.impl;

import com.user_service.entity.User;
import com.user_service.repo.UserRepository;
import com.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new Exception("user not found with email -" + email);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}