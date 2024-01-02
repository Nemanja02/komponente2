package com.komponente.servis2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Pricing", indexes = {
        @Index(name = "pricing_training_type_id_index", columnList = "training_type_id", unique = true)
})
@Getter
@Setter
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private TrainingType trainingType;
    private float price;
}
