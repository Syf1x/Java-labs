package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.exception.TransactionTestException;
import com.office.employeemanagement.mapper.EmployeeMapper;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.model.EmployeeProfile;
import com.office.employeemanagement.model.Task;
import com.office.employeemanagement.repository.DepartmentRepository;
import com.office.employeemanagement.repository.EmployeeRepository;
import com.office.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());

        EmployeeProfile profile = new EmployeeProfile();
        profile.setBio(dto.getBio());
        employee.setProfile(profile);

        if (dto.getDepartmentId() != null) {
            departmentRepository.findById(dto.getDepartmentId())
                    .ifPresent(employee::setDepartment);
        }

        if (dto.getTaskIds() != null && !dto.getTaskIds().isEmpty()) {
            List<Task> tasks = taskRepository.findAllById(dto.getTaskIds());
            employee.setTasks(new HashSet<>(tasks));
        }

        return EmployeeMapper.toDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto saveWithTransactionCheck(EmployeeDto dto) {
        EmployeeDto savedDto = createEmployee(dto);
        if ("error".equalsIgnoreCase(dto.getFirstName())) {
            throw new TransactionTestException("Тестовая ошибка для проверки транзакции");
        }
        return savedDto;
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
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}