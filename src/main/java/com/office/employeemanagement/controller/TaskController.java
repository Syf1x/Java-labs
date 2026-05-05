package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.TaskDto;
import com.office.employeemanagement.service.TaskService;
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
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public TaskDto create(@RequestBody TaskDto dto) {
        return taskService.create(dto);
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable Long id, @RequestBody TaskDto dto) {
        return taskService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}