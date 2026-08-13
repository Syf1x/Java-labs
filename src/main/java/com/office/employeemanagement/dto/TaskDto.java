package com.office.employeemanagement.dto;

import com.office.employeemanagement.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Объект переноса данных задачи")
public record TaskDto(
        @Schema(description = "Уникальный идентификатор", example = "1")
        Long id,

        @NotBlank(message = "Заголовок задачи обязателен для заполнения")
        @Size(min = 2, max = 150, message = "Заголовок должен быть от 2 до 150 символов")
        @Schema(description = "Заголовок задачи", example = "Настроить CI")
        String title,

        @Schema(description = "Описание задачи", example = "Добавить пайплайн сборки и тестов")
        String description,

        @Schema(description = "Статус задачи")
        TaskStatus status,

        @Schema(description = "ID проекта", example = "1")
        Long projectId
) { }
