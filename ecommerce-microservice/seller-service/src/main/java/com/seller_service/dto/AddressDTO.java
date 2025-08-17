package com.seller_service.dto;


import com.seller_service.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long id;
    private String name;
    private String locality;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String mobile;

    // Convert from Entity to DTO
    public static AddressDTO from(Address address) {
        if (address == null) return null;
        return new AddressDTO(
                address.getId(),
                address.getName(),
                address.getLocality(),
                address.getAddress(),
                address.getCity(),
                address.getState(),
                address.getPinCode(),
                address.getMobile()
        );
    }

    // Convert from DTO to Entity
    public static Address to(AddressDTO dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setId(dto.getId());
        address.setName(dto.getName());
        address.setLocality(dto.getLocality());
        address.setAddress(dto.getAddress());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPinCode(dto.getPinCode());
        address.setMobile(dto.getMobile());
        return address;
    }
}