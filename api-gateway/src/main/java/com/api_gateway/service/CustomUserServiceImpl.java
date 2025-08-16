package com.api_gateway.service;

import com.api_gateway.domain.USER_ROLE;
import com.api_gateway.dto.SellerDTO;
import com.api_gateway.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private static final String SELLER_PREFIX = "seller_";
    private final WebClient.Builder webClientBuilder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());

            SellerDTO sellerByEmail = webClientBuilder.build()
                    .get()
                    .uri("http://SELLER-SERVICE/sellers/find/email/{email}", actualUsername)
                    .retrieve()
                    .bodyToMono(SellerDTO.class)
                    .block(); // Blocking here because UserDetailsService is synchronous

            if (sellerByEmail != null) {
                return buildUserDetails(sellerByEmail.getEmail(), sellerByEmail.getPassword(), sellerByEmail.getRole());
            }

        } else {
            UserDTO user = webClientBuilder.build()
                    .get()
                    .uri("http://USER-SERVICE/users/find/email/{email}", username)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();

            if (user != null) {
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
            }
        }
        throw new UsernameNotFoundException("user or seller not found with email -> " + username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if (role == null) role = USER_ROLE.ROLE_CUSTOMER;

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorityList
        );
    }
}