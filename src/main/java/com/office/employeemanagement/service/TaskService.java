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

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskDto getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return convertToDto(task);
    }

    @Transactional
    public TaskDto create(TaskDto dto) {
        Task task = new Task();
        updateTaskFromDto(task, dto);
        return convertToDto(taskRepository.save(task));
    }

    @Transactional
    public TaskDto update(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        updateTaskFromDto(task, dto);
        return convertToDto(task);
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }

        taskRepository.deleteFromEmployeeTasks(id);

        taskRepository.deleteById(id);
    }

    private void updateTaskFromDto(Task task, TaskDto dto) {
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status() != null ? dto.status() : TaskStatus.TODO);
        if (dto.projectId() != null) {
            projectRepository.findById(dto.projectId()).ifPresent(task::setProject);
        }
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