package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public EmployeeDto create(@RequestBody EmployeeDto dto) {
        return employeeService.createEmployee(dto);
    }

    @GetMapping
    public List<EmployeeDto> findAll() {
        return employeeService.getAllEmployees();
    }
}