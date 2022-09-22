package com.udacity.jdnd.course3.critter.pet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.customer.Customer;
import com.udacity.jdnd.course3.critter.user.customer.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.customer.service.CustomerNotFoundException;

import javax.transaction.Transactional;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public List<PetDTO> getPets() {
        List<Pet> listPets = (List<Pet>) petRepository.findAll();
        List<PetDTO> listPetDTOs = new ArrayList<PetDTO>();
        PetDTO petDTO = new PetDTO();
        for (Pet pet : listPets) {
            petDTO = convertPetToPetDto(pet);
            listPetDTOs.add(petDTO);
        }

        return listPetDTOs;
    }

    public PetDTO save(PetDTO petDto) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDto, pet);
        pet.setType(petDto.getType().toString());

        Customer customer = customerRepository.findById(petDto.getOwnerId())
                .orElseThrow(CustomerNotFoundException::new);

        pet.setCustomer(customer);
        petRepository.save(pet);
        List<Pet> listPets = (List<Pet>) petRepository.findAll();
        petDto.setId(listPets.get(listPets.size() - 1).getId());

        return petDto;
    }

    public PetDTO getPet(long petId) {
        Pet pet = getPetById(petId);
        PetDTO petDTO = convertPetToPetDto(pet);

        return petDTO;
    }

    public List<PetDTO> getPetsByOwner(long userId) {
        List<PetDTO> petDTOs = new ArrayList<>();
        List<Pet> pets = petRepository.findByCustomerId(userId);
        PetDTO petDTO = new PetDTO();
        for (Pet pet : pets) {
            petDTO = convertPetToPetDto(pet);
            petDTOs.add(petDTO);
        }

        return petDTOs;
    }

    private Pet getPetById(long petId) {
        return petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
    }

    private PetDTO convertPetToPetDto(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setType(PetType.valueOf(pet.getType()));
        petDTO.setOwnerId(pet.getCustomer().getId());

        return petDTO;
    }
}
