package com.api_gateway.controller;

import com.api_gateway.domain.USER_ROLE;
import com.api_gateway.request.LoginRequest;
import com.api_gateway.response.AuthResponse;
import com.api_gateway.response.UserResponse;
import com.api_gateway.service.JwtService;
import com.api_gateway.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

//    private final UserClient userServiceClient;
//    private final JwtService jwtService;
//    @PostMapping("/signing")
//    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
//        UserResponse user = userServiceClient.validateUser(loginRequest);
//
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "Invalid credentials"));
//        }
//        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
//
//        return ResponseEntity.ok(Map.of("token", token));
//    }


    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    @PostMapping("/signing")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {
        return webClientBuilder.build()
                .post()
                .uri("http://user-service/auth/signing/verify")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .flatMap(userResponse -> {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(userResponse.getRoles()));

                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userResponse.getUsername(),
                                null,
                                authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String token = jwtUtil.generateToken(authentication);
                        AuthResponse response = new AuthResponse();
                        response.setJwt(token);
                        response.setMessage("Register Success");
                        response.setRole(USER_ROLE.valueOf(userResponse.getRoles()));
                        return Mono.just(ResponseEntity.ok(response));

                });
    }

    @PostMapping("/signing/seller")
    public Mono<ResponseEntity<?>> loginSeller(@RequestBody LoginRequest req) throws Exception {
        req.setEmail("seller_" + req.getEmail());
        return webClientBuilder.build()
                .post()
                .uri("http://user-service/auth/signing/verify")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .flatMap(userResponse -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(userResponse.getRoles()));

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userResponse.getUsername(),
                            null,
                            authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = jwtUtil.generateToken(authentication);
                    AuthResponse response = new AuthResponse();
                    response.setJwt(token);
                    response.setMessage("Register Success");
                    response.setRole(USER_ROLE.valueOf(userResponse.getRoles()));
                    return Mono.just(ResponseEntity.ok(response));

                });

    }
}
