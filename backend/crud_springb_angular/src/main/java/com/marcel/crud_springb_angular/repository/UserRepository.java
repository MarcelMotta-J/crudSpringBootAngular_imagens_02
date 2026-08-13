package com.marcel.crud_springb_angular.repository;



import com.marcel.crud_springb_angular.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);


    
}
