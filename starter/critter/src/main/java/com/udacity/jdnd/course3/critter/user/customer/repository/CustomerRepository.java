package com.udacity.jdnd.course3.critter.user.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course3.critter.user.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByPets_Id(long petId);
}
