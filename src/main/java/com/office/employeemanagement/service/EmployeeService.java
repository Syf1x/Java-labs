package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.dto.SearchKey;
import com.office.employeemanagement.exception.ResourceNotFoundException;
import com.office.employeemanagement.exception.TransactionTestException;
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
import java.util.Optional;
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
        Page<EmployeeDto> result = employeeRepository.findAllWithPagination(dept, name, pageable)
                .map(this::convertToDto);
        cache.put(key, result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDto> getFilteredEmployeesNative(String dept, String name, Pageable pageable) {
        return employeeRepository.findAllNativeWithPagination(dept, name, pageable)
                .map(this::convertToDto);
    }

    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        invalidateCache();
        return convertToDto(employeeRepository.save(buildEmployee(dto)));
    }

    @Transactional
    public List<EmployeeDto> createBulk(List<EmployeeDto> dtos) {
        invalidateCache();
        log.info("Bulk-создание {} сотрудников", dtos.size());
        return dtos.stream()
                .map(this::buildEmployee)
                .map(employeeRepository::save)
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeDto dto) {
        invalidateCache();
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        EmployeeProfile profile = employee.getProfile();
        if (profile == null) {
            profile = new EmployeeProfile();
            profile.setEmployee(employee);
            employee.setProfile(profile);
        }
        profile.setBio(dto.bio());
        profile.setPhoneNumber(dto.phoneNumber());
        if (dto.departmentId() != null) {
            departmentRepository.findById(dto.departmentId()).ifPresent(employee::setDepartment);
        } else {
            employee.setDepartment(null);
        }
        return convertToDto(employee);
    }

    @Transactional
    public void delete(Long id) {
        invalidateCache();
        employeeRepository.deleteById(id);
    }

    @Transactional
    public EmployeeDto assignTask(Long employeeId, Long taskId) {
        invalidateCache();
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (employee.getTasks() == null) {
            employee.setTasks(new HashSet<>());
        }
        employee.getTasks().add(task);
        return convertToDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeDto unassignTask(Long employeeId, Long taskId) {
        invalidateCache();
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
        if (employee.getTasks() != null) {
            employee.getTasks().removeIf(task -> task.getId().equals(taskId));
        }
        return convertToDto(employeeRepository.save(employee));
    }

    public void createPartialWrite(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        employeeRepository.save(employee);
        throw new RuntimeException("Искусственная ошибка для проверки частичной записи: "
                + "метод без @Transactional, сохранённая запись останется в БД");
    }

    @Transactional
    public void createBulkTransactional(List<EmployeeDto> dtos) {
        log.info("Транзакционное bulk-создание {} сотрудников с искусственной ошибкой в конце", dtos.size());
        for (EmployeeDto dto : dtos) {
            employeeRepository.save(buildEmployee(dto));
        }
        throw new TransactionTestException("Искусственная ошибка после сохранения " + dtos.size()
                + " сотрудников: метод с @Transactional, все изменения будут отменены");
    }

    private Employee buildEmployee(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.firstName());
        employee.setLastName(dto.lastName());
        EmployeeProfile profile = new EmployeeProfile();
        profile.setBio(dto.bio());
        profile.setPhoneNumber(dto.phoneNumber());
        profile.setEmployee(employee);
        employee.setProfile(profile);
        Optional.ofNullable(dto.departmentId())
                .flatMap(departmentRepository::findById)
                .ifPresent(employee::setDepartment);
        return employee;
    }

    private void invalidateCache() {
        log.info("Инвалидация кэша: данные изменены, кэш очищен.");
        cache.clear();
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeProfile profile = employee.getProfile();
        String bio = (profile != null) ? profile.getBio() : "Нет";
        String phone = (profile != null) ? profile.getPhoneNumber() : "Нет";
        Long deptId = (employee.getDepartment() != null) ? employee.getDepartment().getId() : null;
        String deptName = (employee.getDepartment() != null) ? employee.getDepartment().getName() : "Нет";
        List<Long> taskIds = (employee.getTasks() != null)
                ? employee.getTasks().stream().map(Task::getId).collect(Collectors.toList())
                : Collections.emptyList();
        return new EmployeeDto(employee.getId(), employee.getFirstName(), employee.getLastName(), bio, phone, deptId, deptName, taskIds);
    }
}