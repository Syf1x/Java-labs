package com.office.employeemanagement.controller;

import com.office.employeemanagement.model.EmployeeProfile;
import com.office.employeemanagement.repository.EmployeeProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final EmployeeProfileRepository profileRepository;

    @Operation(summary = "Получить все профили сотрудников")
    @GetMapping
    public List<EmployeeProfile> getAll() {
        return profileRepository.findAll();
    }
}