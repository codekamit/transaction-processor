package com.orm.learn_orm.model;


import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client_preference", schema="orm", uniqueConstraints = {
@UniqueConstraint(name = "uc_client_currency", columnNames = {"client_name", "currency"})})
public class ClientPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_preference_seq")
    @SequenceGenerator(name = "client_preference_seq", sequenceName = "client_preference_sequence", allocationSize = 50)
    private Long id;

    @Column(name="client_name", nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name="settlement_level", nullable = false)
    private SettlementLevel settlementLevel;

    @Enumerated(EnumType.STRING)
    @Column(name="currency", nullable = false)
    private Currency currency;

    @Column(name="netting", nullable = false)
    private boolean netting;

    @OneToMany(mappedBy = "clientPreference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FundGroup> fundMapping = new ArrayList<>();

    @Column(name="status", nullable = false)
    private Status status;
}