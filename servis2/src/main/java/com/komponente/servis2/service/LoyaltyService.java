package com.komponente.servis2.service;

import com.komponente.servis2.client.userservice.dto.UserDto;
import com.komponente.servis2.dto.LoyaltyRewardDto;
import com.komponente.servis2.entity.Gym;
import com.komponente.servis2.entity.LoyaltyRewards;
import com.komponente.servis2.entity.TrainingType;
import com.komponente.servis2.mapper.LoyaltyRewardMapper;
import com.komponente.servis2.repository.LoyaltyRewardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoyaltyService {
    @Autowired
    private LoyaltyRewardsRepository loyaltyRewardsRepository;
    @Autowired
    private LoyaltyRewardMapper loyaltyRewardMapper;
    @Autowired
    UserService userService;
    @Autowired
    PricingService pricingService;
    public void addReward(Gym gym, Long threshold, String description, Long discount) {
        LoyaltyRewards loyaltyRewards = new LoyaltyRewards();
        loyaltyRewards.setGym(gym);
        loyaltyRewards.setThreshold(threshold);
        loyaltyRewards.setDescription(description);
        loyaltyRewards.setDiscount(discount);

        loyaltyRewardsRepository.save(loyaltyRewards);
    }

    public LoyaltyRewardDto updateReward(LoyaltyRewardDto loyaltyRewardDto) {
        LoyaltyRewards loyaltyRewards = loyaltyRewardsRepository.findById(loyaltyRewardDto.getId()).orElseThrow(() -> new RuntimeException("Loyalty reward not found"));
        loyaltyRewards.setThreshold(loyaltyRewardDto.getThreshold());
        loyaltyRewards.setDescription(loyaltyRewardDto.getDescription());
        loyaltyRewards.setDiscount(loyaltyRewardDto.getDiscount());
        loyaltyRewardsRepository.save(loyaltyRewards);
        return loyaltyRewardDto;
    }

    public Iterable<LoyaltyRewardDto> getAllRewards() {
        Iterable<LoyaltyRewards> loyaltyRewards = loyaltyRewardsRepository.findAll();
        return loyaltyRewardMapper.toDto(loyaltyRewards);
    }

    public Iterable<LoyaltyRewardDto> getAllRewardsForGym(Long gymId) {
        Iterable<LoyaltyRewards> loyaltyRewards = loyaltyRewardsRepository.findAllByGymId(gymId);
        return loyaltyRewardMapper.toDto(loyaltyRewards);
    }

    public Iterable<LoyaltyRewardDto> getAllForManager(Long managerId) {
        UserDto manager = userService.getManager(managerId);
        return getAllRewardsForGym(manager.getGymId());
    }

    public Float getPrice(Gym gym, TrainingType trainingType, Long userId) {
        Integer count = userService.getTrainingCountForGym(gym, userId);
        Iterable<LoyaltyRewards> loyaltyRewards = loyaltyRewardsRepository.findAllByGymId(gym.getId());
        try {
            for (LoyaltyRewards loyaltyReward : loyaltyRewards) {
                if (count >= loyaltyReward.getThreshold()) {
                    return pricingService.getPricingByTrainingTypeId(trainingType.getId(), true).getPrice() * (100 - loyaltyReward.getDiscount()) / 100;
                }
            }
        } catch (Exception e) {
            return 0F;
        }
        return pricingService.getPricingByTrainingTypeId(trainingType.getId(), true).getPrice();
    }
}
