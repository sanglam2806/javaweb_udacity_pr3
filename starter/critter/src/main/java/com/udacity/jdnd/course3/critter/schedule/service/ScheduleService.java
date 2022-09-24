package com.udacity.jdnd.course3.critter.schedule.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.user.employee.Employee;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(schedule.getId());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(new HashSet<>());
        schedule.getActivities().addAll(scheduleDTO.getActivities());

        schedule.setPets(scheduleDTO.getPetIds() == null ? Collections.emptyList()
                : scheduleDTO.getPetIds().stream().map(Pet::new).collect(Collectors.toList()));
        schedule.setEmployees(scheduleDTO.getEmployeeIds() == null ? Collections.emptyList()
                : scheduleDTO.getEmployeeIds().stream().map(Employee::new).collect(Collectors.toList()));

        return toDto(scheduleRepository.save(schedule));
    }

    public List<ScheduleDTO> getAll() {
        return scheduleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        return scheduleRepository.findByEmployees_Id(employeeId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForPet(long petId) {
        return scheduleRepository.findByPets_Id(petId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        List<Schedule> pets = scheduleRepository.findByCustomerId(customerId);

        return pets.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ScheduleDTO toDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        scheduleDTO.setActivities(new HashSet<>(schedule.getActivities()));
        return scheduleDTO;
    }
}
