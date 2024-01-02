package com.komponente.servis2.repository;

import com.komponente.servis2.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    Optional<Pricing> findByTrainingTypeId(Long trainingTypeId);

    boolean existsByTrainingTypeId(Long trainingTypeId);

    void deleteByTrainingTypeId(Long trainingTypeId);

}
