package com.rentrideweb.RentRideWebsite.controller;

import com.rentrideweb.RentRideWebsite.dto.AdminRequest;
import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDto;
import com.rentrideweb.RentRideWebsite.entity.Car;
import com.rentrideweb.RentRideWebsite.services.admin.AdminService;
import com.rentrideweb.RentRideWebsite.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "https://rent-ride-website-brown.vercel.app"})
public class AdminController {
    private final AdminService adminService;
    private final AuthService authService;

    @PostMapping("/car")
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto) throws IOException {

        boolean suc = false;

        suc = adminService.postCar(carDto);

        if(!suc){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/car")
    public ResponseEntity<?> getCats(){
        return ResponseEntity.ok(adminService.getAllCars());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id){
        adminService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id){
        CarDto carDto = adminService.getCarById(id);
        return ResponseEntity.ok(carDto);
    }

    @PutMapping("/car/{carId}")
    public ResponseEntity<Void> updateCar(@PathVariable Long carId, @ModelAttribute CarDto carDto){
        try{
            boolean suc = adminService.updateCar(carId, carDto);

            if(suc){
                return ResponseEntity.status(HttpStatus.OK).build();
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @GetMapping("/car/bookings")
    public ResponseEntity<List<BookACarDto>> getBookings(){
        System.out.println(adminService.getBookings());
        return ResponseEntity.ok(adminService.getBookings());
    }

    @GetMapping("/car/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status){
        return adminService.changeBookingStatus(bookingId, status) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();

    }

    @PostMapping("admin")
    public ResponseEntity<?> adminReg(@RequestBody AdminRequest adminRequest){
        boolean suc = authService.createAdmin(adminRequest);
        return suc ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/admin/{email}")
    public ResponseEntity<?> adminDelete(@PathVariable String email){
        if(Objects.equals(email, "admin@rentride.com")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean suc = authService.deleteByEmail(email);
        return suc ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
