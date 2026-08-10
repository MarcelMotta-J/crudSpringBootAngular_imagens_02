package com.marcel.crud_springb_angular.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.marcel.crud_springb_angular.dto.UserDTO;

import com.marcel.crud_springb_angular.repository.UserRepository;


import jakarta.transaction.Transactional;

import com.marcel.crud_springb_angular.entity.User;

import org.springframework.beans.factory.annotation.Value;

@Service
public class UserService {



    @Autowired
    private UserRepository userRepository;
    

    public List<UserDTO> getAllUsers() {
        
        return userRepository.findAll().stream()
            .map(user-> new UserDTO(user.getId(), user.getFirstname(),
                user.getLastname(), user.getEmail(), user.getPassword(),
                user.getActive(), user.getProfileImage()))
            .collect(Collectors.toList());
    }

    
    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        return new UserDTO(user.getId(), user.getFirstname(),
                user.getLastname(), user.getEmail(), user.getPassword(),
                user.getActive(), user.getProfileImage());
    }




     
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        userRepository.delete(user);
    }


    @Transactional
    public UserDTO saveUser(User user) throws  IOException{


        User savedUser = userRepository.save(user);

        return new UserDTO(user.getId(), user.getFirstname(),
                user.getLastname(), user.getEmail(), user.getPassword(),
                user.getActive(), user.getProfileImage());

    }


    @Transactional
    public UserDTO updateUser(Long id, User user) throws  IOException{
        User existingUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setProfileImage(user.getProfileImage());



        existingUser.setActive(user.getActive());

        User updateUser = userRepository.save(existingUser);

        return new UserDTO(updateUser.getId(), updateUser.getFirstname(),
                updateUser.getLastname(), updateUser.getEmail(), updateUser.getPassword(),
                updateUser.getActive(), updateUser.getProfileImage());
    }

}