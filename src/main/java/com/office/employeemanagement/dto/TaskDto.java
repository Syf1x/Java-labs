package com.office.employeemanagement.dto;

import com.office.employeemanagement.model.TaskStatus;

public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Long projectId
) { }