package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.ProjectDto;
import com.office.employeemanagement.service.ProjectService;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "Получить все проекты")
    @GetMapping
    public List<ProjectDto> getAll() {
        return projectService.getAll();
    }

    @Operation(summary = "Получить проект по ID")
    @GetMapping("/{id}")
    public ProjectDto getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @Operation(summary = "Создать проект")
    @PostMapping
    public ProjectDto create(@Valid @RequestBody ProjectDto dto) {
        return projectService.create(dto);
    }

    @Operation(summary = "Обновить проект")
    @PutMapping("/{id}")
    public ProjectDto update(@PathVariable Long id, @Valid @RequestBody ProjectDto dto) {
        return projectService.update(id, dto);
    }

    @Operation(summary = "Удалить проект")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }
}