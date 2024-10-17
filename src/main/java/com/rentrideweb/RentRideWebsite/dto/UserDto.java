package com.rentrideweb.RentRideWebsite.dto;

import com.rentrideweb.RentRideWebsite.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String name;
    private String email;
    private UserRole userRole;
}
