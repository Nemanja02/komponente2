package com.komponente.servis2.controller;

import com.komponente.servis2.dto.LoyaltyRewardDto;
import com.komponente.servis2.service.LoyaltyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward")
public class RewardController {
    @Autowired
    private LoyaltyService loyaltyService;

    @ApiOperation(value = "Get all rewards for gym")
    @GetMapping("/{gymId}")
    public ResponseEntity<Iterable<LoyaltyRewardDto>> getAllRewardsForGym(@PathVariable Long gymId) {
        return ResponseEntity.ok(loyaltyService.getAllRewardsForGym(gymId));
    }

    @ApiOperation(value = "Edit reward")
    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyRewardDto> editReward(@PathVariable Long id, @RequestBody LoyaltyRewardDto loyaltyRewardDto) {
        return ResponseEntity.ok(loyaltyService.updateReward(loyaltyRewardDto));
    }
}
