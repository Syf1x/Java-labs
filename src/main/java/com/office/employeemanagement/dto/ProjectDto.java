package com.office.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Объект переноса данных проекта")
public record ProjectDto(
        @Schema(description = "Уникальный идентификатор", example = "1")
        Long id,

        @NotBlank(message = "Название проекта обязательно для заполнения")
        @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
        @Schema(description = "Название проекта", example = "Employee Management")
        String name,

        @Schema(description = "Описание проекта", example = "Система управления сотрудниками")
        String description
) { }
