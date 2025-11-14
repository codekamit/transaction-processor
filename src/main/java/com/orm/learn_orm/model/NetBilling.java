package com.orm.learn_orm.model;


import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.marker_interface.ISettlementNetted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "net_billing", schema = "orm")
public class NetBilling implements ISettlementNetted {

    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 16)
    private String id;
    @Column(name = "broker_number", nullable = false)
    private String brokerShortname;
    @Column(name = "payment_broker_number", nullable = false)
    private String paymentBrokerNumber;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "currency", nullable = false)
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_level", nullable = false)
    private SettlementLevel settlementLevel;
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_type", nullable = false)
    private SettlementType settlementType;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;

    @PrePersist
    @PreUpdate
    public void convertToUpperCase() {
        if (this.brokerShortname != null) {
            this.brokerShortname = this.brokerShortname.toUpperCase();
        }
        if (this.paymentBrokerNumber != null) {
            this.paymentBrokerNumber = this.paymentBrokerNumber.toUpperCase();
        }
    }
}
