package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.TaskDto;
import com.office.employeemanagement.model.Task;
import com.office.employeemanagement.model.TaskStatus;
import com.office.employeemanagement.repository.ProjectRepository;
import com.office.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskDto create(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status() != null ? dto.status() : TaskStatus.TODO);

        if (dto.projectId() != null) {
            projectRepository.findById(dto.projectId()).ifPresent(task::setProject);
        }

        return convertToDto(taskRepository.save(task));
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject() != null ? task.getProject().getId() : null
        );
    }
}