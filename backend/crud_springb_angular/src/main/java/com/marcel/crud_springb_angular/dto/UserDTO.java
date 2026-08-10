package com.marcel.crud_springb_angular.dto;


public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        Boolean active,
        String profileImage
) {
}