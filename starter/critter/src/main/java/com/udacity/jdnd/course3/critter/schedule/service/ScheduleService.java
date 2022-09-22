package com.udacity.jdnd.course3.critter.schedule.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.user.employee.Employee;

import javax.transaction.Transactional;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private PetRepository petRepository;

    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(schedule.getId());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(new HashSet<>());
        schedule.getActivities().addAll(scheduleDTO.getActivities());

        schedule.setPets(
                scheduleDTO.getPetIds() == null ? Collections.emptyList() : scheduleDTO.getPetIds().stream().map(id -> {
                    Pet pet = new Pet();
                    pet.setId(id);
                    return pet;
                }).collect(Collectors.toList()));
        schedule.setEmployees(scheduleDTO.getEmployeeIds() == null ? Collections.emptyList()
                : scheduleDTO.getEmployeeIds().stream().map(id -> {
                    Employee employee = new Employee();
                    employee.setId(id);
                    return employee;
                }).collect(Collectors.toList()));

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
        List<Pet> pets = petRepository.findByCustomerId(customerId);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<ScheduleDTO>();
        pets.stream().forEach(pet -> {
            scheduleDTOs.addAll(scheduleRepository.findByPets_Id(pet.getId()).stream().map(this::toDto)
                    .collect(Collectors.toList()));
        });

        return scheduleDTOs;
    }

    private ScheduleDTO toDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        scheduleDTO.setActivities(new HashSet<>());
        scheduleDTO.getActivities().addAll(schedule.getActivities());
        return scheduleDTO;
    }
}
