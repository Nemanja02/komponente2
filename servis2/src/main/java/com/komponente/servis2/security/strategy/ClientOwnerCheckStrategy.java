package com.komponente.servis2.security.strategy;

import com.komponente.servis2.security.CustomUserDetails;
import com.komponente.servis2.service.OwnershipService;
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
