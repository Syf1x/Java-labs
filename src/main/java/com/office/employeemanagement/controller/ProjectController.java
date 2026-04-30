package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.ProjectDto;
import com.office.employeemanagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ProjectDto create(@RequestBody ProjectDto dto) {
        return projectService.create(dto);
    }
}