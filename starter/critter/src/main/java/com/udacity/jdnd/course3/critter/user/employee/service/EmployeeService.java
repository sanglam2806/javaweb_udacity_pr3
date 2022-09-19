package com.udacity.jdnd.course3.critter.user.employee.service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.employee.Employee;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeSchedule;
import com.udacity.jdnd.course3.critter.user.employee.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.employee.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.employee.repository.EmployeeScheduleRepository;
import com.udacity.jdnd.course3.critter.user.employee.repository.EmployeeSkillRepository;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private EmployeeRepository employeeRepository;
    private EmployeeSkillRepository employeeSkillRepository;
    private EmployeeScheduleRepository employeeScheduleRepository;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeSkillRepository employeeSkillRepository,
            EmployeeScheduleRepository employeeScheduleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.employeeScheduleRepository = employeeScheduleRepository;
    }

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeRepository.save(employee);

        if (employeeDTO.getSkills() != null && employeeDTO.getSkills().size() > 0)
            employeeDTO.getSkills().stream().forEach(skill -> {
                EmployeeSkill employeeSkill = new EmployeeSkill(employee, skill.toString());
                employeeSkillRepository.save(employeeSkill);
            });

        if (employeeDTO.getDaysAvailable() != null && employeeDTO.getDaysAvailable().size() > 0) {
            employeeDTO.getDaysAvailable().stream().forEach(day -> {
                EmployeeSchedule employeeSchedule = new EmployeeSchedule(employee, day.toString());
                employeeScheduleRepository.save(employeeSchedule);
            });
        }

        List<Employee> employees = employeeRepository.findAll();
        employeeDTO.setId(employees.get(employees.size() - 1).getId());

        return employeeDTO;
    }

    public EmployeeDTO getEmployee(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);

        employeeDTO.setSkills(new HashSet<>());

        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findByEmployeeId(employeeId);
        employeeSkills.stream().forEach(skill -> {
            employeeDTO.getSkills().add(com.udacity.jdnd.course3.critter.user.EmployeeSkill.valueOf(skill.getSkill()));
        });

        employeeDTO.setDaysAvailable(new HashSet<>());

        List<EmployeeSchedule> employeeSchedules = employeeScheduleRepository.findByEmployeeId(employeeId);
        employeeSchedules.stream().forEach(schedule -> {
            employeeDTO.getDaysAvailable().add(DayOfWeek.valueOf(schedule.getDay()));
        });

        return employeeDTO;
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        daysAvailable.stream().forEach(day -> {
            EmployeeSchedule schedule = new EmployeeSchedule();
            schedule.setEmployee(employee);
            schedule.setDay(day.toString());
            employeeScheduleRepository.save(schedule);
        });
    }

    private static final RowMapper<Employee> employeeRowMapper = new BeanPropertyRowMapper<>(Employee.class);

    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();

        Set<String> inclSkills = new HashSet<String>();
        employeeDTO.getSkills().stream().forEach(skill -> {
            inclSkills.add(skill.toString());
        });

        String queryStr = "Select employee.id, employee.name from employee"
                + " inner join employee_skill on employee.id = employee_skill.user_id"
                + " inner join employee_schedule on employee.id = employee_schedule.user_id"
                + " where employee_schedule.day like :day and employee_skill.skill in (:inclSkills)";

        List<Employee> employees = jdbcTemplate.query(queryStr,
                new MapSqlParameterSource()
                        .addValue("day", employeeDTO.getDate().getDayOfWeek().toString())
                        .addValue("inclSkills", inclSkills),
                employeeRowMapper);

        Set<Employee> sEmployees = new HashSet<>();
        for (Employee employee : employees) {
            List<EmployeeSkill> skills = employeeSkillRepository.findByEmployeeId(employee.getId());
            Set<String> skillStrs = new HashSet<>();

            skillStrs.addAll(skills.stream().map(skill -> skill.getSkill()).collect(Collectors.toList()));

            if (inclSkills.containsAll(skillStrs) || (skillStrs.containsAll(inclSkills))) {
                sEmployees.add(employee);
            }
        }

        sEmployees.stream().forEach(employee -> {
            EmployeeDTO employeeDTOobj = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTOobj);
            employeeDTOs.add(employeeDTOobj);
        });

        return employeeDTOs;
    }
}
