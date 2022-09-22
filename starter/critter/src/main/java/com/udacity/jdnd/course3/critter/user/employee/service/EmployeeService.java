package com.udacity.jdnd.course3.critter.user.employee.service;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.Skill;
import com.udacity.jdnd.course3.critter.user.employee.Employee;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeSchedule;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.employee.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.employee.repository.EmployeeScheduleRepository;
import com.udacity.jdnd.course3.critter.user.employee.repository.EmployeeSkillRepository;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final EmployeeScheduleRepository employeeScheduleRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeSkillRepository employeeSkillRepository,
                           EmployeeScheduleRepository employeeScheduleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.employeeScheduleRepository = employeeScheduleRepository;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        Employee saved = employeeRepository.save(employee);

        if (employeeDTO.getSkills() != null && employeeDTO.getSkills().size() > 0) {
            List<EmployeeSkill> employeeSkills = employeeDTO.getSkills().stream()
                    .map(skill -> new EmployeeSkill(employee, skill.toString()))
                    .collect(Collectors.toList());
            employeeSkillRepository.saveAll(employeeSkills);
        }

        if (employeeDTO.getDaysAvailable() != null && employeeDTO.getDaysAvailable().size() > 0) {
            List<EmployeeSchedule> employeeSkills = employeeDTO.getDaysAvailable().stream()
                    .map(day -> new EmployeeSchedule(employee, day.toString()))
                    .collect(Collectors.toList());
            employeeScheduleRepository.saveAll(employeeSkills);
        }

        return toDto(saved);
    }

    public EmployeeDTO getEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);

        return toDto(employee);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        daysAvailable.forEach(day -> {
            EmployeeSchedule schedule = new EmployeeSchedule();
            schedule.setEmployee(employee);
            schedule.setDay(day.toString());
            employeeScheduleRepository.save(schedule);
        });
    }

    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        Set<String> inclSkills = employeeDTO.getSkills().stream().map(Enum::toString).collect(Collectors.toSet());
        String dayOfWeek = employeeDTO.getDate().getDayOfWeek().toString();

        List<Employee> bySkillAndDay =
                employeeRepository.findByEmployeeSkills_SkillInAndEmployeeSchedules_Day(inclSkills, dayOfWeek);

        return bySkillAndDay.stream().map(this::toDto).collect(Collectors.toList());
    }

    private EmployeeDTO toDto(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);

        employeeDTO.setDaysAvailable(
                employee.getEmployeeSchedules() == null ? Collections.emptySet()
                        : employee.getEmployeeSchedules().stream()
                        .map(schedule -> DayOfWeek.valueOf(schedule.getDay()))
                        .collect(Collectors.toSet()));

        employeeDTO.setSkills(
                employee.getEmployeeSkills() == null ? Collections.emptySet()
                        : employee.getEmployeeSkills().stream()
                        .map(skill -> Skill.valueOf(skill.getSkill()))
                        .collect(Collectors.toSet()));

        return employeeDTO;
    }
}
