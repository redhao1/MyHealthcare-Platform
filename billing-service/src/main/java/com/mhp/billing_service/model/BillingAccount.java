package com.mhp.billing_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "billing-account")
public class BillingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String status;

    @Column(unique = true)
    private UUID patientId;

    @Column
    private String name;

    @Column
    private String eamil;
}
