package com.rentrideweb.RentRideWebsite.repositary;

import com.rentrideweb.RentRideWebsite.dto.BookACarDto;
import com.rentrideweb.RentRideWebsite.entity.BookACar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookACarRepositary extends JpaRepository<BookACar, Long> {

    List<BookACar> findAllByUserId(long userId);
}
