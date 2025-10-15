package com.orm.learn_orm.model;

import com.orm.learn_orm.SettlementLinkStatus;
import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "net_earning", schema = "orm")
public class NetEarning {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
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
    private List<UUID> lastLinkedTo;
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
    @OneToMany(mappedBy = "netEarning", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Earning> earnings = new ArrayList<>();
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

    public void addEarning(Earning earning) {
        if (this.earnings == null) {
            this.earnings = new ArrayList<>();
        }
        this.earnings.add(earning);
        earning.setNetEarning(this);
    }

    public void unlinkEarnings() {
        if (this.earnings != null && !this.earnings.isEmpty()) {
            this.lastLinkedTo = this.earnings.stream()
                    .map(Earning::getId)
                    .collect(Collectors.toList());

            this.earnings.forEach(earning -> earning.setNetEarning(null));
            this.earnings.clear();
        }

        this.linkStatus = SettlementLinkStatus.SUSPENDED;
    }
}

