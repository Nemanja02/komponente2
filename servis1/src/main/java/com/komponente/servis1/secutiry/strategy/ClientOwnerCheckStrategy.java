package com.komponente.servis1.secutiry.strategy;

import com.komponente.servis1.secutiry.CustomUserDetails;
import com.komponente.servis1.service.impl.OwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientOwnerCheckStrategy implements SecurityCheckStrategy{

    @Autowired
    private OwnershipService ownershipService;

    @Override
    public boolean check(CustomUserDetails userDetails, Long resourceId) {
        return ownershipService.isOwnerOfClient(userDetails.getId(), resourceId);
    }
}
