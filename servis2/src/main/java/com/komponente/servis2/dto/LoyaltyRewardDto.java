package com.komponente.servis2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoyaltyRewardDto {
    private Long id;
    private Long gymId;
    private Long threshold;
    private Long discount;
    private String description;

    public LoyaltyRewardDto() {
    }

    public LoyaltyRewardDto(Long id, Long gymId, Long threshold, Long discount, String description) {
        this.id = id;
        this.gymId = gymId;
        this.threshold = threshold;
        this.discount = discount;
        this.description = description;
    }
}
