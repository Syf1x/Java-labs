package com.office.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Стандартный формат ответа об ошибке")
public record ApiError(
        @Schema(description = "HTTP статус")
        int status,
        @Schema(description = "Текст ошибки")
        String message,
        @Schema(description = "Время возникновения")
        LocalDateTime timestamp,
        @Schema(description = "Путь, вызвавший ошибку")
        String path
) { }