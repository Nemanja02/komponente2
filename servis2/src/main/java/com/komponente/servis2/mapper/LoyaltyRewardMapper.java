package com.komponente.servis2.mapper;

import com.komponente.servis2.dto.LoyaltyRewardDto;
import com.komponente.servis2.entity.LoyaltyRewards;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoyaltyRewardMapper {
    public LoyaltyRewardDto toDto(LoyaltyRewards entity) {
        LoyaltyRewardDto dto = new LoyaltyRewardDto();
        dto.setId(entity.getId());
        dto.setGymId(entity.getGym().getId());
        dto.setThreshold(entity.getThreshold());
        dto.setDiscount(entity.getDiscount());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public LoyaltyRewards toEntity(LoyaltyRewardDto dto) {
        LoyaltyRewards entity = new LoyaltyRewards();
        entity.setId(dto.getId());
        entity.setThreshold(dto.getThreshold());
        entity.setDiscount(dto.getDiscount());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public Iterable<LoyaltyRewardDto> toDto(Iterable<LoyaltyRewards> entities) {
        List<LoyaltyRewardDto> dtos = new java.util.ArrayList<>();
        for (LoyaltyRewards entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }
}
