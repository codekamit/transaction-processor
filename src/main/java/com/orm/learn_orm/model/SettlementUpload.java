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
@Table(name="settlement_upload", schema="orm")
public class SettlementUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadId;
    @Column(name="file_name", nullable = false)
    private String fileName;
    @Column(name="file_type", nullable = false)
    private String fileType;
    @Column(name="file_size", nullable = false)
    private String fileSize;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_upload_id")
    private List<Earning> earnings;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_upload_id")
    private List<NetEarning> netEarnings;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_upload_id")
    private List<Billing> billings;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_upload_id")
    private List<NetBilling> netBillings;
}
