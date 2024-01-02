package com.komponente.servis1.service.impl;

import com.komponente.servis1.repository.ClientUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnershipService {

    @Autowired
    private ClientUserRepository clientUserRepository;

    public boolean isOwnerOfClient(Long tokenID, Long clientID) {
        return tokenID.equals(clientID);
    }
}
