package com.udacity.jdnd.course3.critter.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.udacity.jdnd.course3.critter.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByEmployees_Id(long employeeId);

    List<Schedule> findByPets_Id(long petId);

    @Query("SELECT S FROM Schedule S JOIN S.pets P WHERE P.customer.id = :customerId")
    List<Schedule> findByCustomerId(long customerId);
}
