package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.mapper.EmployeeMapper;
import com.office.employeemanagement.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();
    private Long nextId = 1L;

    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        employee.setId(nextId++);
        employees.add(employee);
        return EmployeeMapper.toDto(employee);
    }

    public List<EmployeeDto> getAllEmployees() {
        return employees.stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }
}