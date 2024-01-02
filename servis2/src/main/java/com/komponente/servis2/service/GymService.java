package com.komponente.servis2.service;

import com.komponente.servis2.dto.GymDto;
import com.komponente.servis2.dto.validations.GymCreateDto;
import com.komponente.servis2.entity.Gym;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.mapper.GymMapper;
import com.komponente.servis2.mapper.TrainingTypeMapper;
import com.komponente.servis2.repository.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GymService {
    GymRepository gymRepository;
    GymMapper gymMapper;
    TrainingTypeMapper trainingTypeMapper;

    public GymService(GymRepository gymRepository, GymMapper gymMapper, TrainingTypeMapper trainingTypeMapper) {
        this.gymRepository = gymRepository;
        this.gymMapper = gymMapper;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    public GymDto add(GymCreateDto gymCreateDto) {
        Gym gym = gymMapper.gymCreateDtoToGym(gymCreateDto);
        gymRepository.save(gym);
        System.out.println("Gym added: " + gym);

        return gymMapper.gymToGymDto(gym);
    }

    public GymDto update(GymCreateDto gymCreateDto, Long id) {
        Gym gym = gymRepository.findById(id).orElseThrow(() -> new RuntimeException("Gym not found"));
        gym.setName(gymCreateDto.getName());
        gym.setDescription(gymCreateDto.getDescription());
        gym.setManagerId(gymCreateDto.getManagerId());
        gym.setNumberOfTrainers(gymCreateDto.getNumberOfTrainers());
        Set<TrainingType> trainingTypes = new HashSet<>();
        gymCreateDto.getTrainingTypes().forEach(trainingType -> {
            trainingTypes.add(trainingTypeMapper.trainingTypeDtoToTrainingType(trainingTypeMapper.longToTrainingTypeDto(trainingType)));
        });
        gym.setTrainingTypes(trainingTypes);
        gymRepository.save(gym);
        System.out.println("Gym updated: " + gym);

        return gymMapper.gymToGymDto(gym);
    }

    public GymDto delete(Long id) {
        Gym gym = gymRepository.findById(id).orElseThrow(() -> new RuntimeException("Gym not found"));
        gymRepository.delete(gym);
        System.out.println("Gym deleted: " + gym);

        return gymMapper.gymToGymDto(gym);
    }

    public GymDto get(Long id) {
        Gym gym = gymRepository.findById(id).orElseThrow(() -> new RuntimeException("Gym not found"));
        System.out.println("Gym found: " + gym);

        return gymMapper.gymToGymDto(gym);
    }

    public Iterable<GymDto> getAll() {
        Iterable<Gym> gyms = gymRepository.findAll();
        System.out.println("Gyms found: " + gyms);

        return gymMapper.gymToGymDto(gyms);
    }
}
