package com.orm.learn_orm.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="billing", schema="orm")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_seq")
    @SequenceGenerator(name = "billing_seq", sequenceName = "billing_sequence", allocationSize = 1000)
    private Long id;
    @Column(name="received_amount", nullable = false)
    private Double receivedAmount;
    @Column(name="currency", nullable = false)
    private String currency;
    @Column(name="broker_number", nullable = false)
    private String brokerNumber;
    @Column(name="broker_shortname", nullable = false)
    private String brokerShortname;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;
}
