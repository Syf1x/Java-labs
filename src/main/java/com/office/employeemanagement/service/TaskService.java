package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.TaskDto;
import com.office.employeemanagement.model.Task;
import com.office.employeemanagement.repository.ProjectRepository;
import com.office.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public TaskDto create(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        if (dto.projectId() != null) {
            projectRepository.findById(dto.projectId()).ifPresent(task::setProject);
        }
        Task saved = taskRepository.save(task);
        return new TaskDto(saved.getId(), saved.getTitle(), saved.getDescription(),
                saved.getProject() != null ? saved.getProject().getId() : null);
    }
}