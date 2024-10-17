package com.rentrideweb.RentRideWebsite.services.customer;


import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDto;
import com.rentrideweb.RentRideWebsite.dto.CarDtoListDto;
import com.rentrideweb.RentRideWebsite.dto.SearchACarDto;
import com.rentrideweb.RentRideWebsite.entity.BookACar;
import com.rentrideweb.RentRideWebsite.entity.Car;
import com.rentrideweb.RentRideWebsite.entity.User;
import com.rentrideweb.RentRideWebsite.enums.BookACarStatus;
import com.rentrideweb.RentRideWebsite.repositary.BookACarRepositary;
import com.rentrideweb.RentRideWebsite.repositary.CarRepositary;
import com.rentrideweb.RentRideWebsite.repositary.UserRepositaty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServicesImpl implements CustomerServices{
    private final CarRepositary carRepositary;
    private final UserRepositaty userRepositaty;
    private final BookACarRepositary bookACarRepositary;

    @Override
    public List<CarDto> getAllCars() {
        return carRepositary.findAll().stream().map(Car::carDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepositary.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepositaty.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()){
            Car existCar = optionalCar.get();

            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existCar);
            bookACar.setBookACarStatus(BookACarStatus.PENDING);

            long diffInMillisec = bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMillisec);

            bookACar.setToDate(bookACarDto.getToDate());
            bookACar.setFromDate(bookACarDto.getFromDate());

            bookACar.setName(bookACarDto.getName());
            bookACar.setPhone(bookACarDto.getPhone());
            bookACar.setAddress(bookACarDto.getAddress());

            bookACar.setDates(days);
            bookACar.setPrice(existCar.getPrice() * days);

            bookACarRepositary.save(bookACar);

            return true;
        }

        return false;
    }

    @Override
    public CarDto getCarById(long carId) {
        Optional<Car> optionalCar = carRepositary.findById(carId);
        return optionalCar.map(Car::carDto).orElse(null);

    }

    @Override
    public List<BookACarDto> getBookingByUsername(long userId) {
        return bookACarRepositary.findAllByUserId(userId).stream()
                .map(BookACar:: getBookACarDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarDtoListDto searchCar(SearchACarDto searchACarDto) {
        Car car = new Car();

        // Set car attributes from search DTO
        car.setBrand(searchACarDto.getBrand());
        car.setType(searchACarDto.getType());
        car.setColor(searchACarDto.getColor());
        car.setTransmission(searchACarDto.getTransmission());


        // Create an example matcher with case-insensitive matching
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "name", "description", "price", "year", "image")
                .withIgnoreNullValues();

        // Create an example instance for query
        Example<Car> carExample = Example.of(car, exampleMatcher);

        // Perform the search query
        List<Car> carList = carRepositary.findAll(carExample);


        // Convert list of Cars to CarDtoListDto
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::carDto).collect(Collectors.toList()));

        return carDtoListDto;
    }

    public Optional<BookACar> getBookingById(long id){
        return bookACarRepositary.findById(id);
    }

}
