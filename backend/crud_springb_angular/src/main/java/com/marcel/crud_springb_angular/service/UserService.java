package com.marcel.crud_springb_angular.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marcel.crud_springb_angular.dto.UserDTO;
import com.marcel.crud_springb_angular.entity.User;
import com.marcel.crud_springb_angular.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getEmail(),
                        user.getActive(),
                        user.getProfileImage()
                ))
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getActive(),
                user.getProfileImage()
        );
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    @Transactional
    public UserDTO saveUser(User user) throws IOException {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getId(),
                savedUser.getFirstname(),
                savedUser.getLastname(),
                savedUser.getEmail(),
                savedUser.getActive(),
                savedUser.getProfileImage()
        );
    }

    @Transactional
    public UserDTO updateUser(Long id, User user) throws IOException {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());

        existingUser.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        existingUser.setProfileImage(user.getProfileImage());
        existingUser.setActive(user.getActive());

        User updatedUser = userRepository.save(existingUser);

        return new UserDTO(
                updatedUser.getId(),
                updatedUser.getFirstname(),
                updatedUser.getLastname(),
                updatedUser.getEmail(),
                updatedUser.getActive(),
                updatedUser.getProfileImage()
        );
    }
}