package com.office.employeemanagement.dto;

public record SearchKey(
        String deptName,
        String lastName,
        int page,
        int size
) {
}