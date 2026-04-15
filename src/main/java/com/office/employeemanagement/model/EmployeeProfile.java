package com.office.employeemanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee_profiles")
@Getter @Setter
public class EmployeeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;
    private String phoneNumber;

    @OneToOne(mappedBy = "profile")
    private Employee eployee;
}