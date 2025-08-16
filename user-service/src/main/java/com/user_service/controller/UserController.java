package com.user_service.controller;

import com.user_service.dto.UserDTO;
import com.user_service.dto.VerificationCodeDTO;
import com.user_service.entity.User;
import com.user_service.entity.VerificationCode;
import com.user_service.repo.VerificationCodeRepository;
import com.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final VerificationCodeRepository verificationCodeRepository;
    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserHandler(
            @RequestHeader("X-User-Email") String email)
            throws Exception {

        return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/api/users/profile")
    public ResponseEntity<UserDTO> getUserFromToken(@RequestHeader("X-User-Email") String email) throws Exception {
        User user = userService.findUserByEmail(email);
        return ResponseEntity.ok(UserDTO.from(user));
    }

    @GetMapping("/api/seller/profile")
    public ResponseEntity<String> getSellerEmailFromToken(@RequestHeader("X-User-Email") String email) throws Exception {
        return ResponseEntity.ok(email);
    }

    @PatchMapping("/seller/verify/{otp}")
    public ResponseEntity<VerificationCodeDTO> verifyEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Invalid OTP");
        }
        return new ResponseEntity<>(VerificationCodeDTO.from(verificationCode), HttpStatus.OK);
    }

    @PostMapping("/seller/save/code")
    public ResponseEntity<VerificationCode> saveSellerCode(@RequestBody VerificationCodeDTO verificationCodeDTO) throws  Exception{
        VerificationCode entity = VerificationCodeDTO.toEntity(verificationCodeDTO);
        return new ResponseEntity<>(verificationCodeRepository.save(entity), HttpStatus.OK);
    }

    @PostMapping("/save/user")
    public ResponseEntity<Void> saveUser(@RequestBody UserDTO userDto) throws Exception{
        User entity = UserDTO.toEntity(userDto);
        userService.saveUser(entity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find/email/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) throws Exception {
        User userByEmail = userService.findUserByEmail(email);
        return new ResponseEntity<>(UserDTO.from(userByEmail), HttpStatus.OK);

    }

}