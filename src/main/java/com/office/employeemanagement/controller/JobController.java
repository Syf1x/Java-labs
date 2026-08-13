package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.dto.JobInfo;
import com.office.employeemanagement.service.AsyncBulkImportWorker;
import com.office.employeemanagement.service.JobRegistry;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobRegistry jobRegistry;
    private final AsyncBulkImportWorker worker;

    @Operation(summary = "Запустить асинхронный bulk-импорт сотрудников, сразу вернув ID задачи")
    @PostMapping("/bulk-import")
    public Map<String, String> startBulkImport(@Valid @RequestBody List<EmployeeDto> dtos) {
        String jobId = jobRegistry.createJob();
        worker.runBulkImport(jobId, dtos);
        return Map.of("jobId", jobId);
    }

    @Operation(summary = "Получить статус асинхронной задачи по ID")
    @GetMapping("/{id}")
    public JobInfo getStatus(@PathVariable String id) {
        return jobRegistry.get(id);
    }
}
