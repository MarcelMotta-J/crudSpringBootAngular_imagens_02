package com.marcel.crud_springb_angular.repository;

import com.marcel.crud_springb_angular.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    Page<User> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstname,
            String lastname,
            String email,
            Pageable pageable
    );
    
}
