package com.rentrideweb.RentRideWebsite.controller;

import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDto;
import com.rentrideweb.RentRideWebsite.dto.SearchACarDto;
import com.rentrideweb.RentRideWebsite.entity.BookACar;
import com.rentrideweb.RentRideWebsite.services.customer.CustomerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = {"http://localhost:4200", "https://rent-ride-website-brown.vercel.app"})
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServices customerServices;

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars(){
        List<CarDto> carDtoList = customerServices.getAllCars();
        return ResponseEntity.ok(carDtoList);

    }

    @PostMapping("/car/book")
    public ResponseEntity<Void> bookACar(@RequestBody BookACarDto bookACarDto){
        boolean suc = customerServices.bookACar(bookACarDto);
        return suc ? ResponseEntity.status(HttpStatus.CREATED).build() :  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/cars/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable long carId){
        CarDto carDto = customerServices.getCarById(carId);

        if (carDto == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(carDto);

    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<BookACarDto>> getBookingByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(customerServices.getBookingByUsername(userId));
    }

    @PostMapping("/search/car")
    public ResponseEntity<?> searchCar(@RequestBody SearchACarDto searchACarDto){
        return ResponseEntity.ok(customerServices.searchCar(searchACarDto));
    }

    @GetMapping("/bookings/get/{id}")
    public ResponseEntity<Optional<BookACar>> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(customerServices.getBookingById(id));
    }

}
