package com.office.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "Объект переноса данных сотрудника")
public record EmployeeDto(
        @Schema(description = "Уникальный идентификатор", example = "1")
        Long id,

        @NotBlank(message = "Имя обязательно для заполнения")
        @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
        @Schema(description = "Имя сотрудника", example = "Иван")
        String firstName,

        @NotBlank(message = "Фамилия обязательна для заполнения")
        @Schema(description = "Фамилия сотрудника", example = "Иванов")
        String lastName,

        @Schema(description = "Биография или описание", example = "Java разработчик с опытом 5 лет")
        String bio,

        @Schema(description = "Контактный номер телефона", example = "+375291234567")
        String phoneNumber,

        @Schema(description = "ID департамента", example = "10")
        Long departmentId,

        @Schema(description = "Название департамента (только для чтения)", example = "IT")
        String departmentName,

        @Schema(description = "Список ID назначенных задач")
        List<Long> taskIds
) { }