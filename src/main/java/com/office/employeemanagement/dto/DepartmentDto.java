package com.office.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Объект переноса данных департамента (только для чтения)")
public record DepartmentDto(
        @Schema(description = "Уникальный идентификатор", example = "1")
        Long id,

        @Schema(description = "Название департамента", example = "IT")
        String name
) {

}
