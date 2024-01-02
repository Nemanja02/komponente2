package com.komponente.servis2.mapper;

import com.komponente.servis2.dto.PricingDto;
import com.komponente.servis2.dto.validations.PricingCreateDto;
import com.komponente.servis2.entity.Pricing;
import com.komponente.servis2.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricingMapper {
    TrainingService trainingService;
    TrainingTypeMapper trainingTypeMapper;

    public PricingMapper(TrainingService trainingService, TrainingTypeMapper trainingTypeMapper) {
        this.trainingService = trainingService;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    public PricingDto pricingToPricingDto(Pricing pricing) {
        PricingDto pricingDto = new PricingDto();
        pricingDto.setId(pricing.getId());
        pricingDto.setTrainingType(trainingTypeMapper.trainingTypeToTrainingTypeDto(pricing.getTrainingType()));
        pricingDto.setPrice(pricing.getPrice());

        return pricingDto;
    }

    public Pricing pricingDtoToPricing(PricingCreateDto pricingCreateDto) {
        Pricing pricing = new Pricing();
        pricing.setTrainingType(trainingTypeMapper.trainingTypeDtoToTrainingType(trainingService.getType(pricingCreateDto.getTrainingTypeId())));
        pricing.setPrice(pricingCreateDto.getPrice());

        return pricing;
    }

    public Iterable<PricingDto> pricingToPricingDto(Iterable<Pricing> pricings) {
        Iterable<PricingDto> pricingDtos = null;
        List<PricingDto> pricingDtoList = new java.util.ArrayList<>();
        for (Pricing pricing : pricings) {
            pricingDtoList.add(pricingToPricingDto(pricing));
        }
        return pricingDtoList;
    }
}
