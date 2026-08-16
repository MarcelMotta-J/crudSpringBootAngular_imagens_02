package com.marcel.crud_springb_angular.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import com.marcel.crud_springb_angular.validation.ValidCPF;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    private Boolean active;

    //store image profile name or path in database
    //store filename ou path here
    private String profileImage;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ValidCPF
    @Column(unique = true, length = 11)
    private String cpf;


}
