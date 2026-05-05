package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.model.EmployeeProfile;
import com.office.employeemanagement.model.Task;
import com.office.employeemanagement.repository.DepartmentRepository;
import com.office.employeemanagement.repository.EmployeeRepository;
import com.office.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAll() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDto getById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = new Employee();
        mapDtoToEntity(employee, dto);
        return convertToDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        mapDtoToEntity(employee, dto);
        return convertToDto(employee);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private void mapDtoToEntity(Employee employee, EmployeeDto dto) {
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());

        EmployeeProfile profile = employee.getProfile();
        if (profile == null) {
            profile = new EmployeeProfile();
            employee.setProfile(profile);
        }
        profile.setBio(dto.bio());
        profile.setPhoneNumber(dto.phoneNumber());

        if (dto.departmentId() != null) {
            departmentRepository.findById(dto.departmentId()).ifPresent(employee::setDepartment);
        }

        if (dto.taskIds() != null) {
            List<Task> tasksList = taskRepository.findAllById(dto.taskIds());
            employee.setTasks(new HashSet<>(tasksList));
        }
    }

    private EmployeeDto convertToDto(Employee employee) {
        String bio = (employee.getProfile() != null) ? employee.getProfile().getBio() : "No bio";
        String phone = (employee.getProfile() != null) ? employee.getProfile().getPhoneNumber() : "No phone";

        Long deptId = (employee.getDepartment() != null) ? employee.getDepartment().getId() : null;
        String deptName = (employee.getDepartment() != null) ? employee.getDepartment().getName() : "Unassigned";

        List<Long> taskIds = (employee.getTasks() != null)
                ? employee.getTasks().stream().map(Task::getId).collect(Collectors.toList())
                : Collections.emptyList();

        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                bio,
                phone,
                deptId,
                deptName,
                taskIds
        );
    }
}