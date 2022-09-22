package com.udacity.jdnd.course3.critter.user.employee;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

@Entity
@NamedEntityGraph(
        name = "employee-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("employeeSkills"),
                @NamedAttributeNode("employeeSchedules")
        }
)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<EmployeeSkill> employeeSkills;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<EmployeeSchedule> employeeSchedules;

    public Employee() {
    }

    public Employee(long id) {
        this.id = id;
    }

    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Set<EmployeeSkill> getEmployeeSkills() {
        return employeeSkills;
    }

    public void setEmployeeSkills(Set<EmployeeSkill> employeeSkills) {
        this.employeeSkills = employeeSkills;
    }

    public Set<EmployeeSchedule> getEmployeeSchedules() {
        return employeeSchedules;
    }

    public void setEmployeeSchedules(Set<EmployeeSchedule> employeeSchedules) {
        this.employeeSchedules = employeeSchedules;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
