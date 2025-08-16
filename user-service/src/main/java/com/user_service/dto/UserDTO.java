package com.user_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private USER_ROLE role;
    private Set<Long> address = new HashSet<>();
    @JsonIgnore
    private Set<Long> usedCoupons = new HashSet<>();
    private String password;

    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setRole(user.getRole());
        dto.setAddress(user.getAddresses());
        dto.setUsedCoupons(user.getUsedCoupons());
        dto.setPassword(user.getPassword());
        return dto;
    }
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setRole(dto.getRole());

        user.setAddresses(dto.getAddress()); // from List<Long>
        user.setUsedCoupons(dto.getUsedCoupons()); // from Set<Long>
        user.setPassword(dto.getPassword());
        return user;
    }
}