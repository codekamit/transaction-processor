package com.orm.learn_orm.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="earning", schema="orm")
public class Earning {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "earning_seq")
    @SequenceGenerator(name = "earning_seq", sequenceName = "earning_sequence", allocationSize = 5000)
    private Long id;
    @Column(name="currency", nullable = false)
    private String currency;
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
