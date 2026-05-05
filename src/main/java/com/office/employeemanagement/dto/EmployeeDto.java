package com.office.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record EmployeeDto(
        Long id,

        @NotBlank(message = "Имя обязательно")
        @Size(min = 2, message = "Имя должно быть от 2 символов")
        String firstName,

        @NotBlank(message = "Фамилия обязательна")
        String lastName,

        String bio,
        String phoneNumber,
        Long departmentId,
        String departmentName,
        List<Long> taskIds
) { }