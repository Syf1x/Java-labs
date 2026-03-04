package com.office.employeemanagement.mapper;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.model.Employee;

/**
 * Утилитарный класс для преобразования между Entity и DTO.
 */
public final class EmployeeMapper {

    private EmployeeMapper() {
        // Приватный конструктор, так как это утилитарный класс
    }

    /**
     * Преобразует сущность Employee в EmployeeDto.
     */
    public static EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
        }
        return dto;
    }

    /**
     * Преобразует EmployeeDto в сущность Employee.
     */
    public static Employee toEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        return employee;
    }
}