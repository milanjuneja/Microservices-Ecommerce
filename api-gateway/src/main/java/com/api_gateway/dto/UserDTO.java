package com.api_gateway.dto;

import com.api_gateway.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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
}