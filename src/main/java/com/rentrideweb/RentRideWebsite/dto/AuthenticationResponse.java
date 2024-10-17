package com.rentrideweb.RentRideWebsite.dto;

import com.rentrideweb.RentRideWebsite.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private Long userId;
    private UserRole userRole;
}
