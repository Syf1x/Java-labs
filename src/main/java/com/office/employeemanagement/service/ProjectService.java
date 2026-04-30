package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.ProjectDto;
import com.office.employeemanagement.model.Project;
import com.office.employeemanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectDto create(ProjectDto dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        Project saved = projectRepository.save(project);
        return new ProjectDto(saved.getId(), saved.getName(), saved.getDescription());
    }
}