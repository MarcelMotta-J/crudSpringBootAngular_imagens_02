package com.marcel.crud_springb_angular.repository;

import com.marcel.crud_springb_angular.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    List<User> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstname,
            String lastname,
            String email
    );
    
}
