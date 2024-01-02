package com.komponente.servis1.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = {@Index(columnList = "username", unique = true), @Index(columnList = "email", unique = true)})
public class ClientUser extends User{
    private String membershipNumber;
    private int scheduledTrainings;

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public int getScheduledTrainings() {
        return scheduledTrainings;
    }

    public void setScheduledTrainings(int scheduledTrainings) {
        this.scheduledTrainings = scheduledTrainings;
    }
}
