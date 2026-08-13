package com.office.employeemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Департамент")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @NotBlank(message = "Название департамента обязательно для заполнения")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    @Schema(description = "Название департамента", example = "IT")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}