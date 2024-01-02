package com.komponente.servis2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class LoyaltyRewards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    private Gym gym;
    private Long threshold;
    private Long discount;
    private String description;
}
