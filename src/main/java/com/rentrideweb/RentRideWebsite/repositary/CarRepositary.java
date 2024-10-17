package com.rentrideweb.RentRideWebsite.repositary;

import com.rentrideweb.RentRideWebsite.entity.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepositary extends JpaRepository<Car, Long> {

}
