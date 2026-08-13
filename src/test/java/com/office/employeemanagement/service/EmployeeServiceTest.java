package com.office.employeemanagement.service;

import com.office.employeemanagement.dto.EmployeeDto;
import com.office.employeemanagement.exception.ResourceNotFoundException;
import com.office.employeemanagement.exception.TransactionTestException;
import com.office.employeemanagement.model.Department;
import com.office.employeemanagement.model.Employee;
import com.office.employeemanagement.repository.DepartmentRepository;
import com.office.employeemanagement.repository.EmployeeRepository;
import com.office.employeemanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TaskRepository taskRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository, departmentRepository, taskRepository);
    }

    private Employee employee(Long id, String firstName, String lastName) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        return employee;
    }

    @Test
    void getFilteredEmployees_cacheMiss_thenCacheHit() {
        Pageable pageable = PageRequest.of(0, 10);
        when(employeeRepository.findAllWithPagination(eq("IT"), isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(employee(1L, "Ivan", "Petrov"))));

        employeeService.getFilteredEmployees("IT", null, pageable);
        employeeService.getFilteredEmployees("IT", null, pageable);

        verify(employeeRepository, times(1)).findAllWithPagination(eq("IT"), isNull(), any());
    }

    @Test
    void create_invalidatesCache_soNextGetHitsDbAgain() {
        Pageable pageable = PageRequest.of(0, 10);
        when(employeeRepository.findAllWithPagination(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(employee(1L, "Ivan", "Petrov"))));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee(2L, "New", "Employee"));

        employeeService.getFilteredEmployees(null, null, pageable);
        employeeService.create(new EmployeeDto(null, "New", "Employee", null, null, null, null, null));
        employeeService.getFilteredEmployees(null, null, pageable);

        verify(employeeRepository, times(2)).findAllWithPagination(any(), any(), any());
    }

    @Test
    void update_throwsResourceNotFoundException_whenEmployeeMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        EmployeeDto dto = new EmployeeDto(null, "X", "Y", null, null, null, null, null);

        assertThatThrownBy(() -> employeeService.update(99L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_appliesChanges_andInvalidatesCache() {
        Pageable pageable = PageRequest.of(0, 10);
        when(employeeRepository.findAllWithPagination(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(employee(1L, "Old", "Name"))));
        Employee existing = employee(1L, "Old", "Name");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));

        employeeService.getFilteredEmployees(null, null, pageable);
        EmployeeDto dto = new EmployeeDto(1L, "New", "Name", "bio", "123", null, null, null);
        EmployeeDto result = employeeService.update(1L, dto);
        employeeService.getFilteredEmployees(null, null, pageable);

        assertThat(result.firstName()).isEqualTo("New");
        verify(employeeRepository, times(2)).findAllWithPagination(any(), any(), any());
    }

    @Test
    void delete_invalidatesCache_andCallsRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        when(employeeRepository.findAllWithPagination(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of()));

        employeeService.getFilteredEmployees(null, null, pageable);
        employeeService.delete(1L);
        employeeService.getFilteredEmployees(null, null, pageable);

        verify(employeeRepository, times(1)).deleteById(1L);
        verify(employeeRepository, times(2)).findAllWithPagination(any(), any(), any());
    }

    @Test
    void createPartialWrite_savesBeforeThrowing() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee(1L, "A", "B"));
        EmployeeDto dto = new EmployeeDto(null, "A", "B", null, null, null, null, null);

        assertThatThrownBy(() -> employeeService.createPartialWrite(dto))
                .isInstanceOf(RuntimeException.class)
                .isNotInstanceOf(TransactionTestException.class);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createBulkTransactional_savesAllThenThrowsTransactionTestException() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee(1L, "A", "B"));
        List<EmployeeDto> dtos = List.of(
                new EmployeeDto(null, "A", "B", null, null, null, null, null),
                new EmployeeDto(null, "C", "D", null, null, null, null, null)
        );

        assertThatThrownBy(() -> employeeService.createBulkTransactional(dtos))
                .isInstanceOf(TransactionTestException.class);

        verify(employeeRepository, times(2)).save(any(Employee.class));
    }

    @Test
    void createBulk_savesEachDtoAndResolvesDepartment() {
        Department department = new Department();
        department.setId(5L);
        department.setName("IT");
        when(departmentRepository.findById(5L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> {
            Employee e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        List<EmployeeDto> dtos = List.of(
                new EmployeeDto(null, "A", "B", null, null, 5L, null, null),
                new EmployeeDto(null, "C", "D", null, null, null, null, null)
        );

        List<EmployeeDto> result = employeeService.createBulk(dtos);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).departmentId()).isEqualTo(5L);
        verify(employeeRepository, times(2)).save(any(Employee.class));
    }
}
