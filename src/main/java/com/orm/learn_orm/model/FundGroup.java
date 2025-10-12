package com.orm.learn_orm.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="fund_group", schema="orm")
public class FundGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fund_group_seq")
    @SequenceGenerator(name = "fund_group_seq", sequenceName = "fund_group_sequence", allocationSize = 100)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_preference_id")
    private ClientPreference clientPreference;

    @Column(name="fund", nullable = false)
    private String fund;

    @Column(name="payment_fund", nullable = false)
    private String paymentFund;
}