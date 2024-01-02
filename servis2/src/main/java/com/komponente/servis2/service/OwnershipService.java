package com.komponente.servis2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnershipService {

    public boolean isOwnerOfClient(Long tokenID, Long clientID) {
        return tokenID.equals(clientID);
    }
}
