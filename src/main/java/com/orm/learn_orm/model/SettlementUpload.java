package com.orm.learn_orm.model;


import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.UploadStatus;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "upload_seq")
    @SequenceGenerator(name = "upload_seq", sequenceName = "upload_sequence", allocationSize = 20)
    private Long Id;
    @Column(name="file_name", nullable = false)
    private String fileName;
    @Enumerated(EnumType.STRING)
    @Column(name="settlement_type", nullable = false)
    private SettlementType settlementType;
    @Column(name="file_size", nullable = false)
    private String fileSize;
    @OneToMany(mappedBy = "settlementUpload", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Earning> earnings;
    @OneToMany(mappedBy = "settlementUpload", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<NetEarning> netEarnings;
    @OneToMany(mappedBy = "settlementUpload", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Billing> billings;
    @OneToMany(mappedBy = "settlementUpload", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<NetBilling> netBillings;
    @Enumerated(EnumType.STRING)
    @Column(name="upload_status", nullable = false)
    private UploadStatus uploadStatus;
}
