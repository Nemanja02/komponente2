package com.komponente.servis2.service;

import com.komponente.servis2.dto.PricingDto;
import com.komponente.servis2.dto.validations.PricingCreateDto;
import com.komponente.servis2.entity.Pricing;
import com.komponente.servis2.mapper.PricingMapper;
import com.komponente.servis2.repository.PricingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PricingService {

    private PricingRepository pricingRepository;
    private PricingMapper pricingMapper;

    public PricingService(PricingRepository pricingRepository, PricingMapper pricingMapper) {
        this.pricingRepository = pricingRepository;
        this.pricingMapper = pricingMapper;
    }

    public PricingDto add(PricingCreateDto pricingCreateDto) {
        if (pricingRepository.findByTrainingTypeId(pricingCreateDto.getTrainingTypeId()).isPresent()) {
            throw new RuntimeException("Price for training type already exists");
        }
        Pricing pricing = pricingMapper.pricingDtoToPricing(pricingCreateDto);
        pricingRepository.save(pricing);
        System.out.println("Pricing added: " + pricing);

        return pricingMapper.pricingToPricingDto(pricing);
    }

    public PricingDto get(Long id) {
        Pricing pricing = pricingRepository.findById(id).orElseThrow(() -> new RuntimeException("Pricing not found"));
        System.out.println("Pricing found: " + pricing);

        return pricingMapper.pricingToPricingDto(pricing);
    }

    public Iterable<PricingDto> getAll() {
        Iterable<Pricing> pricings = pricingRepository.findAll();
        System.out.println("Pricings found: " + pricings);

        return pricingMapper.pricingToPricingDto(pricings);
    }

    public PricingDto update(PricingCreateDto pricingCreateDto, Long id) {
        Pricing pricing = pricingRepository.findById(id).orElseThrow(() -> new RuntimeException("Pricing not found"));
        pricing.setPrice(pricingCreateDto.getPrice());
        pricingRepository.save(pricing);
        System.out.println("Pricing updated: " + pricing);

        return pricingMapper.pricingToPricingDto(pricing);
    }

    public PricingDto delete(Long id) {
        Pricing pricing = pricingRepository.findById(id).orElseThrow(() -> new RuntimeException("Pricing not found"));

        pricingRepository.delete(pricing);
        System.out.println("Pricing deleted: " + pricing);

        return pricingMapper.pricingToPricingDto(pricing);
    }

    public PricingDto getPricingByTrainingTypeId(Long id, boolean required) {
        Optional<Pricing> pricing = pricingRepository.findByTrainingTypeId(id);
        if (required && !pricing.isPresent()) {
            throw new RuntimeException("Pricing not found");
        }
        if (!pricing.isPresent()) {
            return null;
        }
        System.out.println("Pricing found: " + pricing);

        return pricingMapper.pricingToPricingDto(pricing.get());
    }
}
