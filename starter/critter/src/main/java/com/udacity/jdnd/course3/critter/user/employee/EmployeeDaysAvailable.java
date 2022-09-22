package com.udacity.jdnd.course3.critter.user.employee;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmployeeDaysAvailable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userId;
    private String dayAvailable;

    public EmployeeDaysAvailable() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDayAvailable() {
        return dayAvailable;
    }

    public void setDayAvailable(String dayAvailable) {
        this.dayAvailable = dayAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDaysAvailable that = (EmployeeDaysAvailable) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
