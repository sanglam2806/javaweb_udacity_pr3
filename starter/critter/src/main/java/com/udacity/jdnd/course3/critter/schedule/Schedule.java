package com.udacity.jdnd.course3.critter.schedule;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.udacity.jdnd.course3.critter.user.employee.Employee;

@Embeddable
public class Schedule implements Serializable {
    private long emplyeeId;
    private long petId;
}
