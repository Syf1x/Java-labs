package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.mapper.EmployeeMapper;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.repository.EmployeeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeDto> getAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    public EmployeeDto getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return EmployeeMapper.toDto(employee);
    }

    public List<EmployeeDto> getByPosition(String position) {
        return employeeRepository.findByPosition(position).stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        return EmployeeMapper.toDto(employeeRepository.save(employee));
    }
}