package com.orm.learn_orm.model;


import com.orm.learn_orm.marker_interface.ISettlement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "billing", schema = "orm")
public class Billing implements ISettlement {

    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 16)
    private String id;
    @Column(name = "received_amount", nullable = false)
    private Double receivedAmount;
    @Column(name = "currency", nullable = false)
    private String currency;
    @Column(name = "broker_number", nullable = false)
    private String brokerNumber;
    @Column(name = "broker_shortname", nullable = false)
    private String brokerShortname;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "net_billing_id")
    private NetBilling netBilling;
}
