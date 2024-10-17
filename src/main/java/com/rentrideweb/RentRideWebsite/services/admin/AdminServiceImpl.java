package com.rentrideweb.RentRideWebsite.services.admin;

import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDto;
import com.rentrideweb.RentRideWebsite.entity.BookACar;
import com.rentrideweb.RentRideWebsite.entity.Car;
import com.rentrideweb.RentRideWebsite.enums.BookACarStatus;
import com.rentrideweb.RentRideWebsite.repositary.BookACarRepositary;
import com.rentrideweb.RentRideWebsite.repositary.CarRepositary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final CarRepositary carRepositary;
    private final BookACarRepositary bookACarRepositary;

    @Override
    public boolean postCar(CarDto carDto) throws IOException {
        try {
            Car car = new Car();
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setTransmission(carDto.getTransmission());
            car.setDescription(carDto.getDescription());
            car.setImage(carDto.getImage().getBytes());
            carRepositary.save(car);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepositary.findAll().stream().map(Car:: carDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        carRepositary.deleteById(id);
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepositary.findById(id);
        return optionalCar.map(Car:: carDto).orElse(null);
    }

    @Override
    public boolean updateCar(Long carId, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepositary.findById(carId);
        if(optionalCar.isPresent()){
            Car car = optionalCar.get();

            if (carDto.getImage() != null){
                car.setImage(carDto.getImage().getBytes());
            }
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setDescription(carDto.getDescription());
            car.setColor(carDto.getColor());
            car.setTransmission(carDto.getTransmission());
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            carRepositary.save(car);
            return true;
        }

        return false;
    }

    @Override
    public List<BookACarDto> getBookings() {
        return bookACarRepositary.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long bookingId, String status) {
        Optional<BookACar> bookACar = bookACarRepositary.findById(bookingId);

        if (bookACar.isPresent()){
            BookACar existBooking = bookACar.get();

            if (Objects.equals(status, "Approve")){
                existBooking.setBookACarStatus(BookACarStatus.APPROVED);
            } else {
                existBooking.setBookACarStatus(BookACarStatus.CANCELED);
            }

            bookACarRepositary.save(existBooking);
            return true;
        }

        return false;


    }
}
