package com.office.employeemanagement.dto;

import java.util.List;

public record EmployeeDto(
        Long id,
        String firstName,
        String lastName,
        String bio,
        String phoneNumber,
        Long departmentId,
        String departmentName,
        List<Long> taskIds
) { }