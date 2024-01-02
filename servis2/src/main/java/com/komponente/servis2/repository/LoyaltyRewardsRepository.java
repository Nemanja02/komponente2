package com.komponente.servis2.repository;

import com.komponente.servis2.entity.LoyaltyRewards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyRewardsRepository extends JpaRepository<LoyaltyRewards, Long> {
    Iterable<LoyaltyRewards> findAllByGymId(Long gymId);
}
