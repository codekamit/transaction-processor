package com.orm.learn_orm.model;

import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.SettlementType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="net_earning", schema="orm")
public class NetEarning {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @Column(name="amount", nullable = false)
    private Double amount;
    @Column(name="client_name", nullable = false)
    private String clientName;
    @Column(name="fund", nullable = false)
    private String paymentFund;
    @Enumerated(EnumType.STRING)
    @Column(name="settlement_level", nullable = false)
    private SettlementLevel settlementLevel;
    @Enumerated(EnumType.STRING)
    @Column(name="settlement_type", nullable = false)
    private SettlementType settlementType;
    @Column(name="currency", nullable = false)
    private String currency;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "net_earning_id")
    private List<Earning> earnings;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;
}
