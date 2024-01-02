package com.komponente.servis2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PricingDto {
    private long id;
    private TrainingTypeDto trainingType;
    private float price;

    public PricingDto() {
    }
}
