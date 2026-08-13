package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.ProjectDto;
import com.office.employeemanagement.exception.ResourceNotFoundException;
import com.office.employeemanagement.model.Project;
import com.office.employeemanagement.repository.ProjectRepository;
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
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project(Long id, String name) {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setDescription("desc");
        return project;
    }

    @Test
    void getAll_mapsProjectsToDto() {
        when(projectRepository.findAll()).thenReturn(List.of(project(1L, "Alpha")));

        List<ProjectDto> result = projectService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Alpha");
    }

    @Test
    void getById_throwsResourceNotFoundException_whenMissing() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_returnsDto_whenFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project(1L, "Alpha")));

        ProjectDto result = projectService.getById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Alpha");
    }

    @Test
    void create_savesAndReturnsDto() {
        when(projectRepository.save(any(Project.class))).thenReturn(project(1L, "Beta"));

        ProjectDto result = projectService.create(new ProjectDto(null, "Beta", "desc"));

        assertThat(result.name()).isEqualTo("Beta");
    }

    @Test
    void update_throwsResourceNotFoundException_whenMissing() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.update(99L, new ProjectDto(null, "X", "Y")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_callsRepository() {
        projectService.delete(1L);

        verify(projectRepository).deleteById(1L);
    }
}
