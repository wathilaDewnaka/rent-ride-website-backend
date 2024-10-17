package com.rentrideweb.RentRideWebsite.controller;

import com.rentrideweb.RentRideWebsite.dto.*;
import com.rentrideweb.RentRideWebsite.entity.User;
import com.rentrideweb.RentRideWebsite.repositary.UserRepositaty;
import com.rentrideweb.RentRideWebsite.services.auth.AuthService;
import com.rentrideweb.RentRideWebsite.services.jwt.UserServices;
import com.rentrideweb.RentRideWebsite.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://rent-ride-website-brown.vercel.app"})
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserServices userServices;
    private final JWTUtils jwtUtil;
    private final UserRepositaty userRepository; // Fixed Typo
    private String otp_code;


    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest){
        if (authService.existCustomerEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("Customer email already there", HttpStatus.BAD_REQUEST);
        }

        String numbers = "0123456789";

        Random rndm_method = new Random();

        char[] otp = new char[6];

        for (int i = 0; i < 6; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }

        otp_code = new String(otp);

        boolean suc = authService.sendEmail(signupRequest.getEmail(), otp_code);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> regCustomer(@RequestBody SignupRequest signupRequest){
        System.out.println(otp_code);
        System.out.println(signupRequest.getOtp());
        if (!Objects.equals(otp_code, signupRequest.getOtp())){
            return new ResponseEntity<>("Customer not registered", HttpStatus.BAD_REQUEST);
        }

        UserDto createCustomerDTO = authService.createCustomer(signupRequest);

        if(createCustomerDTO == null){
            return new ResponseEntity<>("Customer not registered", HttpStatus.BAD_REQUEST);
        } else{
            return new ResponseEntity<>(createCustomerDTO, HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            return new ResponseEntity<>("Incorrect username or password.", HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userServices.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/forget")
    public ResponseEntity<?> resetPass(@RequestBody ForgetpasswordRequest forgetpasswordRequest){
        System.out.println(forgetpasswordRequest.getEmail());
        String numbers = "0123456789";

        Random rndm_method = new Random();

        char[] otp = new char[6];

        for (int i = 0; i < 6; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }

        otp_code = new String(otp);

        boolean suc = authService.sendEmail(forgetpasswordRequest.getEmail(), otp_code);
        System.out.println(suc);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassSuc(@RequestBody ForgetpasswordRequest forgetpasswordRequest){
        if (!Objects.equals(otp_code, forgetpasswordRequest.getOtp())){
            return new ResponseEntity<>("Customer not registered", HttpStatus.BAD_REQUEST);
        }

        UserDto userDto = authService.resetPassword(forgetpasswordRequest.getEmail(), forgetpasswordRequest.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
