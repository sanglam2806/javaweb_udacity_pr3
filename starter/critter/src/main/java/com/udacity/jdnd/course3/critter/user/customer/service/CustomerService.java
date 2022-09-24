package com.udacity.jdnd.course3.critter.user.customer.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.customer.Customer;
import com.udacity.jdnd.course3.critter.user.customer.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = toEntity(customerDTO);
        Customer saved = customerRepository.save(customer);
        return toDto(saved);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(this::toDto).collect(Collectors.toList());
    }

    public CustomerDTO getOwnerByPet(long petId) {
        Customer customer = customerRepository.findByPets_Id(petId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }

        return toDto(customer);
    }

    private Customer toEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        return customer;
    }

    private CustomerDTO toDto(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();

        BeanUtils.copyProperties(customer, customerDTO);

        List<Long> petIds = customer.getPets() == null
                ? Collections.emptyList()
                : customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        customerDTO.setPetIds(petIds);

        return customerDTO;
    }

}
