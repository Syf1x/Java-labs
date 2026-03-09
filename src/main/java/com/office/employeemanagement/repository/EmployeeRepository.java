package com.office.employeemanagement.repository;

import com.office.employeemanagement.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();
    private Long nextId = 1L;

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(nextId++);
        }
        employees.add(employee);
        return employee;
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public Optional<Employee> findById(Long id) {
        return employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
}