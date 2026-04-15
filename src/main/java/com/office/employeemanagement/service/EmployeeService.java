package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.mapper.EmployeeMapper;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.model.EmployeeProfile;
import com.office.employeemanagement.repository.EmployeeRepository;
import com.office.employeemanagement.exception.TransactionTestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = EmployeeMapper.toEntity(dto);

        if (dto.getBio() != null) {
            EmployeeProfile profile = new EmployeeProfile();
            profile.setBio(dto.getBio());
            employee.setProfile(profile);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(EmployeeMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void saveWithTransactionCheck(EmployeeDto dto) {
        Employee emp = EmployeeMapper.toEntity(dto);
        employeeRepository.save(emp);

        if ("error".equalsIgnoreCase(dto.getFirstName())) {
            throw new TransactionTestException("Rollback triggered: Данные не будут сохранены в БД");
        }

        Employee secondEmp = new Employee();
        secondEmp.setFirstName("System");
        secondEmp.setLastName("User");
        employeeRepository.save(secondEmp);
    }
}