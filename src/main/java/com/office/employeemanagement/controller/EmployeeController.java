package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeDto create(@RequestBody EmployeeDto dto) {
        return employeeService.createEmployee(dto);
    }

    @GetMapping
    public List<EmployeeDto> findAll() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeDto findById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/test-transaction")
    public void testTransaction(@RequestBody EmployeeDto dto) {
        employeeService.saveWithTransactionCheck(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}