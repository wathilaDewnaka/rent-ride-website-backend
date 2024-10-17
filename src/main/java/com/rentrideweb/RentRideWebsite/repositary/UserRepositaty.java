package com.rentrideweb.RentRideWebsite.repositary;

import com.rentrideweb.RentRideWebsite.entity.User;
import com.rentrideweb.RentRideWebsite.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositaty extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);

    void deleteByEmail(String email);

}
