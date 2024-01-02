package com.komponente.servis2.dto.validations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PricingCreateDto {
    private Long trainingTypeId;
    private float price;
}
