package com.office.employeemanagement.mapper;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.model.Employee;

/**
 * Класс для преобразования (маппинга) сущностей в DTO и обратно.
 */
public final class EmployeeMapper {

    private EmployeeMapper() {
    }

    /**
     * Преобразование сущности Employee в объект EmployeeDto.
     *
     * @param employee сущность из базы данных.
     * @return объект для передачи данных.
     */
    public static EmployeeDto toDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        return dto;
    }

    public static Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        return employee;
    }
}