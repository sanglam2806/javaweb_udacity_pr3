package com.udacity.jdnd.course3.critter.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course3.critter.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByEmployees_Id(long employeeId);

    List<Schedule> findByPets_Id(long petId);
}
