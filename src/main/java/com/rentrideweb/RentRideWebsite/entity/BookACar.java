package com.rentrideweb.RentRideWebsite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.enums.BookACarStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class BookACar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fromDate;
    private Date toDate;
    private Long dates;
    private Long price;
    private BookACarStatus bookACarStatus;
    private String address;
    private String phone;
    private String name;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Car car;



    public BookACarDto getBookACarDto(){
        BookACarDto bookACarDto = new BookACarDto();

        bookACarDto.setId(id);
        bookACarDto.setDates(dates);
        bookACarDto.setBookACarStatus(bookACarStatus);
        bookACarDto.setPrice(price);
        bookACarDto.setToDate(toDate);
        bookACarDto.setFromDate(fromDate);
        bookACarDto.setName(name);
        bookACarDto.setPhone(phone);
        bookACarDto.setAddress(address);
        bookACarDto.setEmail(user.getEmail());
        bookACarDto.setUsername(user.getUsername());
        bookACarDto.setUserId(user.getId());
        bookACarDto.setCarId(car.getId());

        return bookACarDto;

    }


}
