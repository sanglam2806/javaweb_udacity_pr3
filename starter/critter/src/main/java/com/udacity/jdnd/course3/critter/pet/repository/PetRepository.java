package com.udacity.jdnd.course3.critter.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course3.critter.pet.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByCustomerId(long userId);
}
