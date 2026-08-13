package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncBulkImportWorker {

    private final JobRegistry jobRegistry;
    private final EmployeeService employeeService;

    @Async("taskExecutor")
    public void runBulkImport(String jobId, List<EmployeeDto> dtos) {
        log.info("Job {}: старт асинхронного импорта {} сотрудников в потоке {}",
                jobId, dtos.size(), Thread.currentThread().getName());
        jobRegistry.markRunning(jobId);
        try {
            Thread.sleep(3000);
            List<EmployeeDto> created = employeeService.createBulk(dtos);
            jobRegistry.markDone(jobId, created.size());
            log.info("Job {}: завершён, создано {} сотрудников", jobId, created.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            jobRegistry.markFailed(jobId, e.getMessage());
        } catch (RuntimeException e) {
            log.error("Job {}: ошибка выполнения", jobId, e);
            jobRegistry.markFailed(jobId, e.getMessage());
        }
    }
}
