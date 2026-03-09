package com.office.employeemanagement.mapper;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.model.Employee;

public final class EmployeeMapper {
    private EmployeeMapper() { }

    public static EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setCategory(employee.getCategory());
        return dto;
    }

    public static Employee toEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setCategory(dto.getCategory());
        return employee;
    }
}