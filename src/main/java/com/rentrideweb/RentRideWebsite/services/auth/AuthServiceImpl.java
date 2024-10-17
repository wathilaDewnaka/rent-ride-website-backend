package com.rentrideweb.RentRideWebsite.services.auth;

import com.rentrideweb.RentRideWebsite.dto.AdminRequest;
import com.rentrideweb.RentRideWebsite.dto.SignupRequest;
import com.rentrideweb.RentRideWebsite.dto.UserDto;
import com.rentrideweb.RentRideWebsite.entity.User;
import com.rentrideweb.RentRideWebsite.enums.UserRole;
import com.rentrideweb.RentRideWebsite.repositary.UserRepositaty;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepositaty userRepositaty;

    @Autowired
    private JavaMailSender mailSender;

    @PostConstruct
    public void createAdminAccount() {
        try {
            User adminAccount = userRepositaty.findByUserRole(UserRole.ADMIN);
            if (adminAccount == null) {
                User newAdmin = new User();
                newAdmin.setName("Admin");
                newAdmin.setEmail("admin@rentride.com");
                newAdmin.setPassword(new BCryptPasswordEncoder().encode("Admin123"));
                newAdmin.setUserRole(UserRole.ADMIN);
                userRepositaty.save(newAdmin);

                System.out.println("Admin account created successfully!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating admin account: " + e.getMessage());
        }
    }

    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);

        User createdUser = userRepositaty.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

        return userDto;
    }

    @Override
    public boolean existCustomerEmail(String email) {
        return userRepositaty.findFirstByEmail(email).isPresent();
    }

    @Override
    public boolean sendEmail(String email, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Your OTP Code");
            helper.setFrom("rentride2024@gmail.com");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "    .otp-container {" +
                    "        font-family: Arial, sans-serif;" +
                    "        margin: 0 auto;" +
                    "        padding: 20px;" +
                    "        max-width: 600px;" +
                    "        background-color: #f4f4f4;" +
                    "        border-radius: 10px;" +
                    "        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);" +
                    "    }" +
                    "    .otp-header {" +
                    "        font-size: 24px;" +
                    "        color: #333;" +
                    "    }" +
                    "    .otp-code {" +
                    "        font-size: 32px;" +
                    "        font-weight: bold;" +
                    "        margin: 20px 0;" +
                    "        color: #FF4433;" +
                    "    }" +
                    "    .otp-footer {" +
                    "        font-size: 14px;" +
                    "        color: #777;" +
                    "        margin-top: 20px;" +
                    "    }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='otp-container'>" +
                    "    <div class='otp-header'>Your One-Time Password (OTP) Code</div>" +
                    "    <div class='otp-code'>" + otpCode + "</div>" +
                    "    <div class='otp-footer'>" +
                    "        Please use this code to complete your authentication. " +
                    "        If you did not request this, please ignore this email." +
                    "    </div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);

            mailSender.send(message);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserDto resetPassword(String email, String newPassword) {
        // Fetch the user by email
        Optional<User> optionalUser = userRepositaty.findFirstByEmail(email);

        if (!optionalUser.isPresent()) {
            return null; // Or throw a UserNotFoundException if you prefer
        }

        // Get the actual User object from the Optional
        User user = optionalUser.get();

        // Encrypt the new password
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        // Save the updated user
        User updatedUser = userRepositaty.save(user);

        // Prepare a UserDto to return
        UserDto userDto = new UserDto();
        userDto.setId(updatedUser.getId());
        userDto.setEmail(updatedUser.getEmail());

        return userDto;
    }

    @Override
    public boolean createAdmin(AdminRequest adminRequest) {
        Optional<User> optionalUser = userRepositaty.findFirstByEmail(adminRequest.getEmail());

        if (optionalUser.isPresent()) {
            // Update existing user role to ADMIN
            User existingUser = optionalUser.get();

            if(existingUser.getUserRole() == UserRole.ADMIN){
                return false;
            }

            existingUser.setUserRole(UserRole.ADMIN);
            userRepositaty.save(existingUser);
            return true;
        }

        return false;

    }


    @Override
    public boolean deleteByEmail(String email) {
        // Fetch the user by email
        Optional<User> optionalUser = userRepositaty.findFirstByEmail(email);

        if (optionalUser.isPresent()) {
            userRepositaty.deleteByEmail(email);
            return true;

        }

        return false;
    }

}
