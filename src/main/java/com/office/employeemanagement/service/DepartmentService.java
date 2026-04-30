package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.DepartmentDto;
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

    @Transactional
    public DepartmentDto create(DepartmentDto dto) {
        Department dept = new Department();
        dept.setName(dto.name());
        Department saved = departmentRepository.save(dept);
        return new DepartmentDto(saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public List<DepartmentDto> getAll() {
        return departmentRepository.findAll().stream()
                .map(d -> new DepartmentDto(d.getId(), d.getName()))
                .toList();
    }
}