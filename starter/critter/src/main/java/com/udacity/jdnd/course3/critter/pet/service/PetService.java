package com.udacity.jdnd.course3.critter.pet.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.customer.Customer;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetDTO> getPets() {
        List<Pet> listPets = petRepository.findAll();

        return listPets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PetDTO save(PetDTO petDto) {
        Pet pet = toEntity(petDto);
        Pet saved = petRepository.save(pet);
        return toDto(saved);
    }

    public PetDTO getPet(long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
        return toDto(pet);
    }

    public List<PetDTO> getPetsByOwner(long userId) {
        List<Pet> pets = petRepository.findByCustomerId(userId);
        return pets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PetDTO toDto(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setType(PetType.valueOf(pet.getType()));
        petDTO.setOwnerId(pet.getCustomer().getId());

        return petDTO;
    }

    private Pet toEntity(PetDTO petDto) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDto, pet);
        pet.setType(petDto.getType().toString());
        pet.setCustomer(new Customer(petDto.getOwnerId()));

        return pet;
    }
}
