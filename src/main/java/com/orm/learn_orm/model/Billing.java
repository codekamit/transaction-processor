package com.orm.learn_orm.model;


import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.marker_interface.ISettlement;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="billing", schema="orm")
public class Billing implements ISettlement {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
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
