package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.mapper.EmployeeMapper;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        return EmployeeMapper.toDto(repository.save(employee));
    }

    public List<EmployeeDto> getAllEmployees(String category) {
        List<Employee> all = repository.findAll();

        return all.stream()
                .filter(e -> category == null || (e.getCategory() != null
                        && e.getCategory().equalsIgnoreCase(category)))
                .sorted(Comparator.comparing(Employee::getId))
                .map(EmployeeMapper::toDto)
                .toList();
    }

    public EmployeeDto getEmployeeById(Long id) {
        return repository.findById(id)
                .map(EmployeeMapper::toDto)
                .orElse(null);
    }
}