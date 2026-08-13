package com.office.employeemanagement.controller;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Получить сотрудников с фильтрацией по департаменту/фамилии и пагинацией")
    @GetMapping
    public Page<EmployeeDto> getEmployees(@RequestParam(required = false) String dept,
                                          @RequestParam(required = false) String name,
                                          Pageable pageable) {
        return employeeService.getFilteredEmployees(dept, name, pageable);
    }

    @Operation(summary = "То же самое, но через native SQL запрос вместо JPQL")
    @GetMapping("/native")
    public Page<EmployeeDto> getEmployeesNative(@RequestParam(required = false) String dept,
                                                @RequestParam(required = false) String name,
                                                Pageable pageable) {
        return employeeService.getFilteredEmployeesNative(dept, name, pageable);
    }

    @Operation(summary = "Демонстрация partial write без @Transactional: сотрудник сохраняется, "
            + "затем бросается исключение, запись остаётся в БД")
    @PostMapping("/test-partial")
    public void testPartial(@Valid @RequestBody EmployeeDto dto) {
        employeeService.createPartialWrite(dto);
    }

    @Operation(summary = "Демонстрация rollback с @Transactional: сохраняется список сотрудников, "
            + "затем бросается исключение, все записи откатываются")
    @PostMapping("/test-bulk-transactional")
    public void testBulkTransactional(@Valid @RequestBody List<EmployeeDto> dtos) {
        employeeService.createBulkTransactional(dtos);
    }

    @Operation(summary = "Создать сотрудника")
    @PostMapping
    public EmployeeDto create(@Valid @RequestBody EmployeeDto dto) {
        return employeeService.create(dto);
    }

    @Operation(summary = "Массово создать сотрудников одним запросом")
    @PostMapping("/bulk")
    public List<EmployeeDto> createBulk(@Valid @RequestBody List<EmployeeDto> dtos) {
        return employeeService.createBulk(dtos);
    }

    @Operation(summary = "Обновить сотрудника")
    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {
        return employeeService.update(id, dto);
    }

    @Operation(summary = "Удалить сотрудника")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }

    @Operation(summary = "Назначить задачу сотруднику (связь ManyToMany)")
    @PostMapping("/{id}/tasks/{taskId}")
    public EmployeeDto assignTask(@PathVariable Long id, @PathVariable Long taskId) {
        return employeeService.assignTask(id, taskId);
    }

    @Operation(summary = "Снять задачу с сотрудника")
    @DeleteMapping("/{id}/tasks/{taskId}")
    public EmployeeDto unassignTask(@PathVariable Long id, @PathVariable Long taskId) {
        return employeeService.unassignTask(id, taskId);
    }
}