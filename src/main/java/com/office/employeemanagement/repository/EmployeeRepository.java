package com.office.employeemanagement.repository;

import com.office.employeemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT e FROM Employee e JOIN e.department d " +
            "WHERE (:deptName IS NULL OR d.name LIKE %:deptName%) " +
            "AND (:lastName IS NULL OR e.lastName LIKE %:lastName%)",
            countQuery = "SELECT count(e) FROM Employee e JOIN e.department d " +
                    "WHERE (:deptName IS NULL OR d.name LIKE %:deptName%) " +
                    "AND (:lastName IS NULL OR e.lastName LIKE %:lastName%)")
    Page<Employee> findByFilter(
            @Param("deptName") String deptName,
            @Param("lastName") String lastName,
            Pageable pageable
    );
}