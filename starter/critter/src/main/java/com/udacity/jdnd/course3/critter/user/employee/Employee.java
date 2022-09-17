package com.udacity.jdnd.course3.critter.user.employee;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<EmployeeSkill> employeeSkill;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<EmployeeSchedule> employeeSchedules;

    public Set<EmployeeSkill> getEmployeeSkill() {
        return employeeSkill;
    }

    public void setEmployeeSkill(Set<EmployeeSkill> employeeSkill) {
        this.employeeSkill = employeeSkill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Employee() {
    }
}
