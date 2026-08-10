package com.marcel.crud_springb_angular.repository;


import com.marcel.crud_springb_angular.dto.UserDTO;
import com.marcel.crud_springb_angular.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);


    
}
