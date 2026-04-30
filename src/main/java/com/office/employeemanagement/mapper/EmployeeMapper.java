package com.office.employeemanagement.mapper;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.model.Task;
import java.util.stream.Collectors;

public class EmployeeMapper {

    private EmployeeMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static EmployeeDto toDto(Employee entity) {
        if (entity == null) {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());

        if (entity.getProfile() != null) {
            dto.setBio(entity.getProfile().getBio());
        }

        if (entity.getDepartment() != null) {
            dto.setDepartmentId(entity.getDepartment().getId());
            dto.setDepartmentName(entity.getDepartment().getName());
        }

        if (entity.getTasks() != null) {
            dto.setTaskIds(entity.getTasks().stream()
                    .map(Task::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}