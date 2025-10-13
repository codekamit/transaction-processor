package com.orm.learn_orm.model;


import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.marker_interface.ISettlement;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.UUID;

@Getter
@Setter
@Builder
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
    @Column(name="amount", nullable = false)
    private Double amount;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id")
    private SettlementUpload settlementUpload;
}
