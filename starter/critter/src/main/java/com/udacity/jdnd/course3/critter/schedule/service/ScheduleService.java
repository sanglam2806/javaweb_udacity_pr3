package com.udacity.jdnd.course3.critter.schedule.service;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.user.employee.Employee;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(schedule.getId());
        schedule.setDate(scheduleDTO.getDate());
//        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setPets(scheduleDTO.getPetIds() == null ? Collections.emptyList() : scheduleDTO.getPetIds().stream().map(id -> {
            Pet pet = new Pet();
            pet.setId(id);
            return pet;
        }).collect(Collectors.toList()));
        schedule.setEmployees(scheduleDTO.getEmployeeIds() == null ? Collections.emptyList() : scheduleDTO.getEmployeeIds().stream().map(id -> {
            Employee employee = new Employee();
            employee.setId(id);
            return employee;
        }).collect(Collectors.toList()));


        return toDto(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> getAll() {
        return scheduleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private ScheduleDTO toDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        return scheduleDTO;
    }
}
