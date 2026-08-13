package com.office.employeemanagement.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EntityEqualsTest {

    @Test
    void employeeAndTask_canBeAddedToBidirectionalSets_withoutStackOverflow() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("A");
        employee.setLastName("B");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("T");

        Set<Employee> taskEmployees = new HashSet<>();
        taskEmployees.add(employee);
        task.setEmployees(taskEmployees);

        Set<Task> employeeTasks = new HashSet<>();

        assertThatCode(() -> employeeTasks.add(task))
                .as("adding a Task that references back the Employee must not StackOverflow "
                        + "via Lombok-generated equals/hashCode over the bidirectional collections")
                .doesNotThrowAnyException();

        employee.setTasks(employeeTasks);
        assertThatCode(employee::hashCode).doesNotThrowAnyException();
        assertThatCode(task::hashCode).doesNotThrowAnyException();
    }

    @Test
    void equality_isBasedOnIdOnly() {
        Employee e1 = new Employee();
        e1.setId(1L);
        e1.setFirstName("A");

        Employee e2 = new Employee();
        e2.setId(1L);
        e2.setFirstName("Different");

        assertThat(e1).isEqualTo(e2);
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
    }
}
