package com.office.employeemanagement.dto;

import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private Double salary;
    private Long departmentId;
}