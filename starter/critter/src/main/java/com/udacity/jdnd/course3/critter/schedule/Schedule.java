package com.udacity.jdnd.course3.critter.schedule;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Skill;
import com.udacity.jdnd.course3.critter.user.employee.Employee;

@Entity
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Employee.class)
    private List<Employee> employees;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Pet.class)
    private List<Pet> pets;
    private LocalDate date;

    @Column(name = "activities")
    @ElementCollection(targetClass = Skill.class)
    private Set<Skill> activities;

    public Schedule() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Skill> getActivities() {
        return activities;
    }

    public void setActivities(Set<Skill> activities) {
        this.activities = activities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) o;
        return id == schedule.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
