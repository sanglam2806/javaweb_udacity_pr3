package com.udacity.jdnd.course3.critter.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course3.critter.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
