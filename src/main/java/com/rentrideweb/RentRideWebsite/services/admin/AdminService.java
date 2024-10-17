package com.rentrideweb.RentRideWebsite.services.admin;

import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDto;
import com.rentrideweb.RentRideWebsite.entity.Car;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    boolean postCar(CarDto carDto) throws IOException;

    List<CarDto> getAllCars();

    void deleteCar(Long id);

    CarDto getCarById(Long id);

    boolean updateCar(Long carId, CarDto carDto) throws IOException;

    List<BookACarDto> getBookings();

    boolean changeBookingStatus(Long bookingId, String status);



}
