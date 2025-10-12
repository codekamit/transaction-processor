package com.orm.learn_orm.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="net_earning", schema="orm")
public class NetEarning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="amount", nullable = false)
    private Double amount;
    @Column(name="currency", nullable = false)
    private String currency;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "net_earning_id")
    private List<Earning> earnings;
}
