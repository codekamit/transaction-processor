package com.orm.learn_orm.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="billing", schema="orm")
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="amount", nullable = false)
    private Double receivedAmount;
    @Column(name="currency", nullable = false)
    private String currency;
    @Column(name="broker_number", nullable = false)
    private String brokerNumber;
    @Column(name="broker_shortname", nullable = false)
    private String brokerShortname;
}
