package com.orm.learn_orm.model;


import com.orm.learn_orm.config.UuidV7Generator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="fund_group", schema="orm")
public class FundGroup {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_preference_id")
    private ClientPreference clientPreference;

    @Column(name="fund", nullable = false)
    private String fund;

    @Column(name="payment_fund", nullable = false)
    private String paymentFund;
}