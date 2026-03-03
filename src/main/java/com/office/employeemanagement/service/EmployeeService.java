package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.mapper.EmployeeMapper;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Слой бизнес-логики для управления данными сотрудников.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Получение всех сотрудников, отфильтрованных по должности.
     *
     * @param position должность сотрудников.
     * @return список DTO сотрудников.
     */
    public List<EmployeeDto> getByPosition(String position) {
        return employeeRepository.findByPosition(position).stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    /**
     * Получение данных сотрудника по идентификатору.
     *
     * @param id уникальный идентификатор сотрудника.
     * @return DTO сотрудника.
     * @throws RuntimeException если сотрудник не найден.
     */
    public EmployeeDto getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return EmployeeMapper.toDto(employee);
    }

    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        return EmployeeMapper.toDto(employeeRepository.save(employee));
    }
}