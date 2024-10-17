package com.rentrideweb.RentRideWebsite.services.customer;

import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDtoListDto;
import com.rentrideweb.RentRideWebsite.dto.SearchACarDto;
import com.rentrideweb.RentRideWebsite.entity.BookACar;

import java.util.List;
import java.util.Optional;

public interface CustomerServices {
    List<CarDto> getAllCars();

    boolean bookACar(BookACarDto bookACarDto);

    CarDto getCarById(long carId);

    List<BookACarDto> getBookingByUsername(long userId);

    CarDtoListDto searchCar(SearchACarDto searchACarDto);

    Optional<BookACar> getBookingById(long id);

}
