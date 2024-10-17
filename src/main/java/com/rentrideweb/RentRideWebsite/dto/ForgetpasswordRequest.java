package com.rentrideweb.RentRideWebsite.dto;

import lombok.Data;

@Data
public class ForgetpasswordRequest {
    private String email;
    private String otp;
    private String password;
}
