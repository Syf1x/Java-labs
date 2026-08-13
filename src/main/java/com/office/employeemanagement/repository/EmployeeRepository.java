package com.office.employeemanagement.repository;

import com.office.employeemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.department
            LEFT JOIN FETCH e.profile
            LEFT JOIN FETCH e.tasks t
            LEFT JOIN FETCH t.project
            WHERE (:deptName IS NULL OR e.department.name LIKE %:deptName%)
            AND (:lastName IS NULL OR e.lastName LIKE %:lastName%)
        """)
    List<Employee> findAllOptimized(
            @Param("deptName") String deptName,
            @Param("lastName") String lastName
    );

    @Query(value = """
            SELECT e FROM Employee e
            LEFT JOIN FETCH e.department
            WHERE (:deptName IS NULL OR e.department.name LIKE %:deptName%)
            AND (:lastName IS NULL OR e.lastName LIKE %:lastName%)
        """, countQuery = """
            SELECT count(e) FROM Employee e
            WHERE (:deptName IS NULL OR e.department.name LIKE %:deptName%)
            AND (:lastName IS NULL OR e.lastName LIKE %:lastName%)
        """)
    Page<Employee> findAllWithPagination(
            @Param("deptName") String deptName,
            @Param("lastName") String lastName,
            Pageable pageable
    );

    @Query(value = """
            SELECT e.* FROM employees e
            LEFT JOIN departments d ON d.id = e.department_id
            WHERE (:deptName IS NULL OR d.name LIKE CONCAT('%', :deptName, '%'))
            AND (:lastName IS NULL OR e.last_name LIKE CONCAT('%', :lastName, '%'))
        """,
            countQuery = """
            SELECT count(*) FROM employees e
            LEFT JOIN departments d ON d.id = e.department_id
            WHERE (:deptName IS NULL OR d.name LIKE CONCAT('%', :deptName, '%'))
            AND (:lastName IS NULL OR e.last_name LIKE CONCAT('%', :lastName, '%'))
        """,
            nativeQuery = true)
    Page<Employee> findAllNativeWithPagination(
            @Param("deptName") String deptName,
            @Param("lastName") String lastName,
            Pageable pageable
    );
}
