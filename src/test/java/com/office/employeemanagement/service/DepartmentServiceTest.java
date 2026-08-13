package com.office.employeemanagement.service;

import com.office.employeemanagement.exception.ResourceNotFoundException;
import com.office.employeemanagement.model.Department;
import com.office.employeemanagement.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void getAll_returnsAllDepartments() {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("IT");
        when(departmentRepository.findAll()).thenReturn(List.of(dept));

        List<Department> result = departmentService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("IT");
    }

    @Test
    void create_savesDepartment() {
        Department dept = new Department();
        dept.setName("HR");
        when(departmentRepository.save(any(Department.class))).thenReturn(dept);

        Department result = departmentService.create(dept);

        assertThat(result.getName()).isEqualTo("HR");
        verify(departmentRepository).save(dept);
    }

    @Test
    void update_throwsResourceNotFoundException_whenMissing() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());
        Department details = new Department();
        details.setName("X");

        assertThatThrownBy(() -> departmentService.update(99L, details))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_updatesName_whenFound() {
        Department existing = new Department();
        existing.setId(1L);
        existing.setName("Old");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        Department details = new Department();
        details.setName("New");

        Department result = departmentService.update(1L, details);

        assertThat(result.getName()).isEqualTo("New");
    }

    @Test
    void delete_callsRepository() {
        departmentService.delete(1L);

        verify(departmentRepository).deleteById(1L);
    }
}
