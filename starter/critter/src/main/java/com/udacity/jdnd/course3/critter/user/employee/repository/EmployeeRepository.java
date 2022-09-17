package com.udacity.jdnd.course3.critter.user.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course3.critter.user.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
