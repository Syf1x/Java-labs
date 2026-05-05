package com.office.employeemanagement.repository;

import com.office.employeemanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query(value = "DELETE FROM employee_tasks WHERE task_id = :taskId", nativeQuery = true)
    void deleteFromEmployeeTasks(@Param("taskId") Long taskId);
}