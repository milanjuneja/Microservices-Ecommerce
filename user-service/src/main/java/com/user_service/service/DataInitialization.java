package com.user_service.service;

import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import com.user_service.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitialization implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }
    private void initializeAdminUser() {
        String adminUsername = "milan123@gmail.com";

        if(userRepository.findByEmail(adminUsername) == null){
            User adminUser = new User();
            adminUser.setPassword(passwordEncoder.encode("abcd@123"));
            adminUser.setFirstName("Milan");
            adminUser.setLastName("Juneja");
            adminUser.setEmail(adminUsername);
            adminUser.setRole(USER_ROLE.ROLE_ADMIN);
            userRepository.save(adminUser);

        }
    }
}