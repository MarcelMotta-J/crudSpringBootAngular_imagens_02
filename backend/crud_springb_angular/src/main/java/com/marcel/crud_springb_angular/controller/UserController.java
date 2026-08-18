package com.marcel.crud_springb_angular.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.marcel.crud_springb_angular.dto.UserDTO;
import com.marcel.crud_springb_angular.entity.User;
import com.marcel.crud_springb_angular.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.marcel.crud_springb_angular.validation.ValidCPF;
import org.springframework.validation.annotation.Validated;


@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {


    private static final String UPLOAD_DIR = "uploads/";


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> createUser(
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("email") String email,
            @ValidCPF
            @RequestParam("cpf") String cpf,
            @RequestParam("password") String password,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setCpf(cpf);
        user.setPassword(password);
        if (image != null && !image.isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // 🔥 creates uploads folder
            }

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImage(fileName);
        }






        UserDTO createdUser = userService.saveUser(user);

        return ResponseEntity.ok(createdUser);

    }

    @Autowired
    private UserService userService;

    // http://localhost:8081/api/users

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers(){

        List<UserDTO> users =  userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    // http://localhost:8081/api/users/search?query=cheetara"
    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );

        Page<UserDTO> users = userService.searchUsers(query, pageable);

        return ResponseEntity.ok(users);
    }

    // http://localhost:8081/api/users/1
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){

        try {
            UserDTO user = userService.getUserById(id);

            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 404);
            error.put("error", "USER not found");
            error.put("message", e.getMessage());
            error.put("timestamp", LocalDateTime.now());

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 404);
            error.put("error", "USER not found");
            error.put("message", e.getMessage());
            error.put("timestamp", LocalDateTime.now());

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("email") String email,
            @ValidCPF
            @RequestParam("cpf") String cpf,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        User user = new User();

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setCpf(cpf);
        user.setPassword(password);

        if (image != null && !image.isEmpty()) {

            File uploadDir = new File(UPLOAD_DIR);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName =
                    System.currentTimeMillis() + "_" + image.getOriginalFilename();

            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            user.setProfileImage(fileName);
        }

        UserDTO updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserDTO>> getUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );

        Page<UserDTO> users = userService.getUsersPage(pageable);

        return ResponseEntity.ok(users);
    }

}
