package com.rentrideweb.RentRideWebsite.services.auth;

import com.rentrideweb.RentRideWebsite.dto.AdminRequest;
import com.rentrideweb.RentRideWebsite.dto.SignupRequest;
import com.rentrideweb.RentRideWebsite.dto.UserDto;
import com.rentrideweb.RentRideWebsite.entity.User;

import java.util.List;

public interface AuthService {
    UserDto createCustomer(SignupRequest signupRequest);

    boolean existCustomerEmail(String email);

    boolean sendEmail(String email, String otp_code);

    UserDto resetPassword(String email, String newPassword);

    boolean createAdmin(AdminRequest adminRequest);

    boolean deleteByEmail(String email);

}
