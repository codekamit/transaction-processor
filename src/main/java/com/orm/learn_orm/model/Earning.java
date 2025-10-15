package com.orm.learn_orm.model;


import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.State;
import com.orm.learn_orm.marker_interface.ISettlement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="earning", schema="orm")
public class Earning implements ISettlement {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name="currency", nullable = false)
    private Currency currency;
    @Column(name="client_name", nullable = false)
    private String clientName;
    @Column(name="fund", nullable = false)
    private String fund;
    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private State state;
    @Column(name="amount", nullable = false)
    private Double amount;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "net_earning_id")
    private NetEarning netEarning;

    @PrePersist
    @PreUpdate
    public void convertToUpperCase() {
        if (this.fund != null) {
            this.fund = this.fund.toUpperCase();
        }
        if (this.clientName != null) {
            this.clientName = this.clientName.toUpperCase();
        }
    }
}
