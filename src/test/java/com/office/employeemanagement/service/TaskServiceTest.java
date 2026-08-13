package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.TaskDto;
import com.office.employeemanagement.exception.ResourceNotFoundException;
import com.office.employeemanagement.model.Task;
import com.office.employeemanagement.model.TaskStatus;
import com.office.employeemanagement.repository.ProjectRepository;
import com.office.employeemanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setStatus(TaskStatus.TODO);
        return task;
    }

    @Test
    void getById_throwsResourceNotFoundException_whenMissing() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_defaultsStatusToTodo_whenStatusMissing() {
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskDto result = taskService.create(new TaskDto(null, "New task", "desc", null, null));

        assertThat(result.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void delete_throwsResourceNotFoundException_whenMissing() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(taskRepository, never()).deleteFromEmployeeTasks(any());
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void delete_removesEmployeeLinks_thenDeletesTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.delete(1L);

        verify(taskRepository, times(1)).deleteFromEmployeeTasks(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
