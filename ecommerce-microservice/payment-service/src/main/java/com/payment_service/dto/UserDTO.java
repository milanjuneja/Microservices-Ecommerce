package com.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment_service.domain.USER_ROLE;
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
    private List<AddressDTO> address = new ArrayList<>();
    @JsonIgnore
    private Set<Long> usedCoupons = new HashSet<>();
}