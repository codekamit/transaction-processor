package com.orm.learn_orm.model;

import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.SettlementLinkStatus;
import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.State;
import com.orm.learn_orm.marker_interface.ISettlementNetted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "net_earning", schema = "orm")
public class NetEarning implements ISettlementNetted {

    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 16)
    private String id;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "client_name", nullable = false)
    private String clientName;
    @Column(name = "payment_fund", nullable = false)
    private String paymentFund;
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_link_status", nullable = false)
    private SettlementLinkStatus linkStatus = SettlementLinkStatus.LINKED;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "net_earning_last_linked", schema = "orm", joinColumns = @JoinColumn(name = "net_earning_id"))
    @Column(name = "earning_id", nullable = true)
    private List<String> lastLinkedTo;
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_level", nullable = false)
    private SettlementLevel settlementLevel;
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_type", nullable = false)
    private SettlementType settlementType;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;
    @Column(name = "currency", nullable = false)
    private String currency;
    @Column(name = "netting", nullable = false)
    private boolean netting;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;

    @PrePersist
    @PreUpdate
    public void convertToUpperCase() {
        if (this.clientName != null) {
            this.clientName = this.clientName.toUpperCase();
        }
        if (this.paymentFund != null) {
            this.paymentFund = this.paymentFund.toUpperCase();
        }
    }
}

