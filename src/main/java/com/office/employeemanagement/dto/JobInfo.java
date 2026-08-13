package com.office.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус асинхронной задачи")
public record JobInfo(
        @Schema(description = "ID задачи") String id,
        @Schema(description = "Статус выполнения") JobStatus status,
        @Schema(description = "Количество созданных сотрудников (заполняется по завершении)") Integer createdCount,
        @Schema(description = "Текст ошибки, если задача завершилась неудачно") String error
) { }
