package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.ProjectDto;
import com.office.employeemanagement.model.Project;
import com.office.employeemanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<ProjectDto> getAll() {
        return projectRepository.findAll().stream()
                .map(p -> new ProjectDto(p.getId(), p.getName(), p.getDescription()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDto getById(Long id) {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return new ProjectDto(p.getId(), p.getName(), p.getDescription());
    }

    @Transactional
    public ProjectDto create(ProjectDto dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        Project saved = projectRepository.save(project);
        return new ProjectDto(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Transactional
    public ProjectDto update(Long id, ProjectDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(dto.name());
        project.setDescription(dto.description());
        return new ProjectDto(project.getId(), project.getName(), project.getDescription());
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}