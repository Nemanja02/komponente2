package com.komponente.servis2.mapper;

import com.komponente.servis2.dto.GymDto;
import com.komponente.servis2.dto.TrainingTypeDto;
import com.komponente.servis2.dto.validations.GymCreateDto;
import com.komponente.servis2.entity.Gym;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GymMapper {

    TrainingTypeMapper trainingTypeMapper;
    @Autowired
    TrainingService trainingService;
    public GymMapper(TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeMapper = trainingTypeMapper;
    }

    public GymDto gymToGymDto(Gym gym) {
        GymDto gymDto = new GymDto();
        gymDto.setId(gym.getId());
        gymDto.setName(gym.getName());
        gymDto.setDescription(gym.getDescription());
        gymDto.setManagerId(gym.getManagerId());
        gymDto.setNumberOfTrainers(gym.getNumberOfTrainers());
        List<TrainingTypeDto> trainingTypeDtos = new ArrayList<>();
        gym.getTrainingTypes().forEach(trainingType -> {
            trainingTypeDtos.add(trainingTypeMapper.trainingTypeToTrainingTypeDto(trainingType));
        });
        gymDto.setTrainingTypes(trainingTypeDtos);
        return gymDto;
    }

    public Iterable<GymDto> gymToGymDto(Iterable<Gym> gyms) {
        Iterable<GymDto> gymDtos = null;
        List<GymDto> gymDtoList = new ArrayList<>();

        for (Gym gym : gyms) {
            gymDtoList.add(gymToGymDto(gym));
        }
        gymDtos = gymDtoList;

        return gymDtos;
    }

    public Gym gymCreateDtoToGym(GymCreateDto gymDto) {
        Gym gym = new Gym();
        gym.setName(gymDto.getName());
        gym.setDescription(gymDto.getDescription());
        gym.setManagerId(gymDto.getManagerId());
        gym.setNumberOfTrainers(gymDto.getNumberOfTrainers());
        gym.setTrainingTypes(new HashSet<>());
        gymDto.getTrainingTypes().forEach(trainingType -> {
            gym.getTrainingTypes().add(trainingTypeMapper.trainingTypeDtoToTrainingType(trainingService.getType(trainingType)));
        });
        return gym;
    }

    public Gym gymDtoToGym(GymDto gymDto) {
        Gym gym = new Gym();
        gym.setId(gymDto.getId());
        gym.setName(gymDto.getName());
        gym.setDescription(gymDto.getDescription());
        gym.setManagerId(gymDto.getManagerId());
        gym.setNumberOfTrainers(gymDto.getNumberOfTrainers());
        gym.setTrainingTypes(new HashSet<>());
        gymDto.getTrainingTypes().forEach(trainingType -> {
            gym.getTrainingTypes().add(trainingTypeMapper.trainingTypeDtoToTrainingType(trainingType));
        });
        return gym;
    }
}
