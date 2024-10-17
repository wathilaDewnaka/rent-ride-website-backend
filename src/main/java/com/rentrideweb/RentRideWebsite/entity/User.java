package com.rentrideweb.RentRideWebsite.entity;

import com.rentrideweb.RentRideWebsite.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)  // Ensure name is not null
    private String name;

    @Column(nullable = false, unique = true)  // Ensure email is not null and unique
    private String email;

    @Column(nullable = false)  // Ensure password is not null
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)  // Ensure userRole is not null
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // or apply your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // or apply your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // or apply your logic
    }

    @Override
    public boolean isEnabled() {
        return true;  // or apply your logic
    }
}
