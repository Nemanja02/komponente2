package com.komponente.servis2.mapper;

import com.komponente.servis2.dto.TrainingTypeDto;
import com.komponente.servis2.dto.validations.TrainingSessionResponseDto;
import com.komponente.servis2.dto.validations.TrainingTypeCreateDto;
import com.komponente.servis2.dto.validations.TrainingTypeResponseDto;
import com.komponente.servis2.entity.TrainingSession;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.service.PricingService;
import com.komponente.servis2.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingTypeMapper {

    @Autowired
    private TrainingService trainingService;
    @Lazy
    @Autowired
    private PricingMapper pricingMapper;
    @Lazy
    @Autowired
    private PricingService pricingService;
    @Lazy
    @Autowired
    private TrainingTypeMapper trainingTypeMapper;

    @Lazy
    @Autowired
    GymMapper gymMapper;

    public TrainingTypeMapper() {
    }

    public TrainingType trainingTypeCreateDtoToTrainingType(TrainingTypeCreateDto trainingTypeCreateDto) {
        TrainingType trainingType = new TrainingType();
        trainingType.setName(trainingTypeCreateDto.getName());
        trainingType.setGroup(trainingTypeCreateDto.isGroup());

        return trainingType;
    }

    public TrainingTypeDto trainingTypeToTrainingTypeDto(TrainingType trainingType) {
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
        trainingTypeDto.setId(trainingType.getId());
        trainingTypeDto.setName(trainingType.getName());
        trainingTypeDto.setGroup(trainingType.isGroup());

        return trainingTypeDto;
    }

    public Iterable<TrainingTypeDto> trainingTypeToTrainingTypeDto(Iterable<TrainingType> trainingTypes) {
        Iterable<TrainingTypeDto> trainingTypeDtos = null;

        List<TrainingTypeDto> trainingTypeDtoList = new java.util.ArrayList<>();
        for (TrainingType trainingType : trainingTypes) {
            trainingTypeDtoList.add(trainingTypeToTrainingTypeDto(trainingType));
        }
        trainingTypeDtos = trainingTypeDtoList;
        return trainingTypeDtos;
    }

    public TrainingType trainingTypeDtoToTrainingType(TrainingTypeDto trainingTypeDto) {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeDto.getId());
        trainingType.setName(trainingTypeDto.getName());
        trainingType.setGroup(trainingTypeDto.isGroup());

        return trainingType;
    }

    public TrainingTypeDto longToTrainingTypeDto(Long id) {
        return trainingService.getType(id);
    }

    public TrainingTypeResponseDto longToTrainingTypeResponseDto(Long id) {
        return trainingTypeDtoToTrainingTypeResponseDto(trainingService.getType(id));
    }
    public TrainingTypeResponseDto trainingTypeToTrainingTypeResponseDto(TrainingType trainingType) {
        TrainingTypeResponseDto trainingTypeResponseDto = new TrainingTypeResponseDto();
        trainingTypeResponseDto.setId(trainingType.getId());
        trainingTypeResponseDto.setName(trainingType.getName());
        trainingTypeResponseDto.setGroup(trainingType.isGroup());
        trainingTypeResponseDto.setPricing(pricingService.getPricingByTrainingTypeId(trainingType.getId(), false));

        return trainingTypeResponseDto;
    }

    public Iterable<TrainingTypeResponseDto> trainingTypeToTrainingTypeResponseDto(Iterable<TrainingType> trainingTypes) {
        Iterable<TrainingTypeResponseDto> trainingTypeResponseDtos = null;

        List<TrainingTypeResponseDto> trainingTypeResponseDtoList = new java.util.ArrayList<>();
        for (TrainingType trainingType : trainingTypes) {
            trainingTypeResponseDtoList.add(trainingTypeToTrainingTypeResponseDto(trainingType));
        }
        trainingTypeResponseDtos = trainingTypeResponseDtoList;
        return trainingTypeResponseDtos;
    }

    public TrainingTypeResponseDto trainingTypeDtoToTrainingTypeResponseDto(TrainingTypeDto trainingTypeDto) {
        TrainingTypeResponseDto trainingTypeResponseDto = new TrainingTypeResponseDto();
        trainingTypeResponseDto.setId(trainingTypeDto.getId());
        trainingTypeResponseDto.setName(trainingTypeDto.getName());
        trainingTypeResponseDto.setGroup(trainingTypeDto.isGroup());
        trainingTypeResponseDto.setPricing(pricingService.getPricingByTrainingTypeId(trainingTypeDto.getId(), false));

        return trainingTypeResponseDto;
    }


}
