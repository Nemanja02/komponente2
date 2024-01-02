package com.komponente.servis2.security.strategy;

import com.komponente.servis2.security.CustomUserDetails;

public interface SecurityCheckStrategy {
    boolean check(CustomUserDetails userDetails, Long resourceId);
}

