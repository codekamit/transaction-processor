package com.orm.learn_orm.model;


import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.UploadStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "settlement_upload", schema = "orm")
public class SettlementUpload {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_type", nullable = false)
    private SettlementType settlementType;
    @Column(name = "file_size", nullable = false)
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
    @Column(name = "upload_status", nullable = false)
    private UploadStatus uploadStatus;

    public void addNetEarning(NetEarning netEarning) {
        if (this.netEarnings == null) {
            this.netEarnings = new ArrayList<>();
        }
        this.netEarnings.add(netEarning);
        netEarning.setSettlementUpload(this);
    }

    public void addEarning(Earning earning) {
        if (this.earnings == null) {
            this.earnings = new ArrayList<>();
        }
        this.earnings.add(earning);
        earning.setSettlementUpload(this);
    }
}
