package com.orm.learn_orm.model;


import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.SettlementType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="net_billing", schema="orm")
public class NetBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "net_billing_seq")
    @SequenceGenerator(name = "net_billing_seq", sequenceName = "net_billing_sequence", allocationSize = 1000)
    private Long id;
    @Column(name="broker_number", nullable = false)
    private String brokerShortname;
    @Column(name="payment_broker_number", nullable = false)
    private String paymentBrokerNumber;
    @Column(name="amount", nullable = false)
    private Double amount;
    @Column(name="currency", nullable = false)
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(name="settlement_level", nullable = false)
    private SettlementLevel settlementLevel;
    @Enumerated(EnumType.STRING)
    @Column(name="settlement_type", nullable = false)
    private SettlementType settlementType;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "net_billing_id")
    private List<Billing> billings;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;
}
