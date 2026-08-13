package com.office.employeemanagement.controller;

import com.office.employeemanagement.model.Department;
import com.office.employeemanagement.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @Operation(summary = "Получить все департаменты")
    @GetMapping
    public List<Department> getAll() {
        return departmentService.getAll();
    }

    @Operation(summary = "Создать департамент")
    @PostMapping
    public Department create(@Valid @RequestBody Department department) {
        return departmentService.create(department);
    }

    @Operation(summary = "Обновить департамент")
    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @Valid @RequestBody Department department) {
        return departmentService.update(id, department);
    }

    @Operation(summary = "Удалить департамент")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }
}