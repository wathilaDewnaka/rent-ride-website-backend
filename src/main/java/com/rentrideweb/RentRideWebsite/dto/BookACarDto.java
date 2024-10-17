package com.rentrideweb.RentRideWebsite.dto;

import com.rentrideweb.RentRideWebsite.enums.BookACarStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BookACarDto {
    private Long id;
    private Date fromDate;
    private Date toDate;
    private Long dates;
    private Long price;
    private BookACarStatus bookACarStatus;
    private Long carId;
    private Long userId;
    private String address;
    private String phone;
    private String name;

    private String username;
    private String email;

}
