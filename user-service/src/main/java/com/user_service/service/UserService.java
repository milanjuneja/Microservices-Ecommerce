package com.user_service.service;

import com.user_service.dto.UserDTO;
import com.user_service.entity.User;

public interface UserService {
    User findUserByEmail(String email) throws Exception;

    void saveUser(User user);
}