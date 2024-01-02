package com.komponente.servis1.secutiry.strategy;

import com.komponente.servis1.secutiry.CustomUserDetails;

public interface SecurityCheckStrategy {
    boolean check(CustomUserDetails userDetails, Long resourceId);
}
