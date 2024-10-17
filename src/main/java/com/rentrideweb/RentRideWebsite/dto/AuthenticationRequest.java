package com.rentrideweb.RentRideWebsite.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;

}
