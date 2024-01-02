package com.komponente.servis2.dto.validations;

import com.komponente.servis2.dto.PricingDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TrainingTypeResponseDto {
    private Long id;
    private String name;
    private boolean isGroup;
    private PricingDto pricing;

    public TrainingTypeResponseDto() {
    }
}
