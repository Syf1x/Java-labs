package com.office.employeemanagement.service;

import com.office.employeemanagement.exception.ResourceNotFoundException;
import com.office.employeemanagement.model.Department;
import com.office.employeemanagement.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Transactional
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    @Transactional
    public Department update(Long id, Department details) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
        dep.setName(details.getName());
        return dep;
    }
}