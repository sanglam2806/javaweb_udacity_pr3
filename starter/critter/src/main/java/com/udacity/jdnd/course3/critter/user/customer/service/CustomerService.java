package com.udacity.jdnd.course3.critter.user.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.customer.Customer;
import com.udacity.jdnd.course3.critter.user.customer.repository.CustomerRepository;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        customerRepository.save(customer);

        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        customerDTO.setId(customers.get(customers.size() - 1).getId());

        return customerDTO;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
        CustomerDTO customerDTO = new CustomerDTO();

        for (Customer customer : customers) {
            BeanUtils.copyProperties(customer, customerDTO);
            customerDTO.setPetIds(new ArrayList<>());

            List<Pet> pets = petRepository.findByCustomerId(customer.getId());
            pets.stream().forEach(pet -> {
                customerDTO.getPetIds().add(pet.getId());
            });

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
        customerDTO.setPetIds(new ArrayList<>());
        List<Pet> pets = petRepository.findByCustomerId(customer.getId());
        pets.stream().forEach(pet -> {
            customerDTO.getPetIds().add(pet.getId());
        });

        return customerDTO;
    }

}
