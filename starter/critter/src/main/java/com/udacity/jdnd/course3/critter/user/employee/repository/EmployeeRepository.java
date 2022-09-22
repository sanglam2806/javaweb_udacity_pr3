package com.udacity.jdnd.course3.critter.user.employee.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course3.critter.user.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(value = "employee-entity-graph")
    List<Employee> findByEmployeeSkills_SkillInAndEmployeeSchedules_Day(Set<String> skills, String day);

    @Override
    @EntityGraph(value = "employee-entity-graph")
    Optional<Employee> findById(Long id);
}
