package com.dms.user.entity;


import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "dms_m_user_profile")
@Data
public class UserProfile extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String createdBy;
    private String updatedBy;

    private Instant createdAt;
    private Instant updatedAt;


    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String priCountryCode;
    private String primaryNumber;

    private String altCountryCode;
    private String altNumber;

    private String permAddress;
    private String resiAddress;

    @OneToOne
    @JoinColumn(name = "associated_user")
    @JsonBackReference
    private User user;


}
