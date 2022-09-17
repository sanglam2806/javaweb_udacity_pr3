package com.udacity.jdnd.course3.critter.user.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.customer.Customer;
import com.udacity.jdnd.course3.critter.user.customer.repository.CustomerRepository;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customerRepository.save(customer);

        return customerDTO;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
        CustomerDTO customerDTO = new CustomerDTO();

        for (Customer customer : customers) {
            BeanUtils.copyProperties(customer, customerDTO);
            customerDTOs.add(customerDTO);
        }

        return customerDTOs;
    }

    public CustomerDTO getOwnerByPet(long petId) {
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = customerRepository.findByPets_Id(petId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        BeanUtils.copyProperties(customer, customerDTO);

        return customerDTO;
    }

}
