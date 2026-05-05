package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.dto.SearchKey;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.model.EmployeeProfile;
import com.office.employeemanagement.model.Task;
import com.office.employeemanagement.repository.DepartmentRepository;
import com.office.employeemanagement.repository.EmployeeRepository;
import com.office.employeemanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskRepository taskRepository;

    private final Map<SearchKey, Page<EmployeeDto>> cache = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    public Page<EmployeeDto> getFilteredEmployees(String dept, String name, Pageable pageable) {
        SearchKey key = new SearchKey(dept, name, pageable.getPageNumber(), pageable.getPageSize());

        if (cache.containsKey(key)) {
            log.info("Данные получены из кэша для ключа: {}", key);
            return cache.get(key);
        }

        log.info("Кэш пуст. Выполнение запроса к БД для ключа: {}", key);

        Page<EmployeeDto> result = employeeRepository.findByFilter(dept, name, pageable)
                .map(this::convertToDto);

        cache.put(key, result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAll() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDto getById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден с ID: " + id));
    }

    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        invalidateCache();
        Employee employee = new Employee();
        mapDtoToEntity(employee, dto);
        return convertToDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeDto dto) {
        invalidateCache();
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));
        mapDtoToEntity(employee, dto);
        return convertToDto(employeeRepository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        invalidateCache();
        employeeRepository.deleteById(id);
    }

    private void invalidateCache() {
        log.info("Инвалидация кэша: данные были изменены.");
        cache.clear();
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeProfile profile = employee.getProfile();
        String bio = (profile != null) ? profile.getBio() : "Биография не заполнена";
        String phone = (profile != null) ? profile.getPhoneNumber() : "Телефон не указан";

        Long deptId = (employee.getDepartment() != null) ? employee.getDepartment().getId() : null;
        String deptName = (employee.getDepartment() != null) ? employee.getDepartment().getName() : "Отдел не назначен";

        List<Long> taskIds = (employee.getTasks() != null)
                ? employee.getTasks().stream().map(Task::getId).collect(Collectors.toList())
                : Collections.emptyList();

        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                bio,
                phone,
                deptId,
                deptName,
                taskIds
        );
    }

    private void mapDtoToEntity(Employee employee, EmployeeDto dto) {
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());

        EmployeeProfile profile = employee.getProfile();
        if (profile == null) {
            profile = new EmployeeProfile();
            employee.setProfile(profile);
        }
        profile.setBio(dto.bio());
        profile.setPhoneNumber(dto.phoneNumber());

        if (dto.departmentId() != null) {
            departmentRepository.findById(dto.departmentId())
                    .ifPresent(employee::setDepartment);
        }

        if (dto.taskIds() != null) {
            List<Task> tasks = taskRepository.findAllById(dto.taskIds());
            employee.setTasks(new HashSet<>(tasks));
        }
    }
}