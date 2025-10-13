package com.orm.learn_orm.model;


import com.orm.learn_orm.config.UuidV7Generator;
import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client_preference", schema="orm", uniqueConstraints = {
@UniqueConstraint(name = "uc_client_currency", columnNames = {"client_name", "currency"})})
public class ClientPreference {

    @Id
    @GeneratedValue(generator = "uuidv7-generator")
    @GenericGenerator(name = "uuidv7-generator", type = UuidV7Generator.class)
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name="client_name", nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name="settlement_level", nullable = false)
    private SettlementLevel settlementLevel;

    @Enumerated(EnumType.STRING)
    @Column(name="currency", nullable = false)
    private Currency currency;

    @Column(name="netting", nullable = false)
    private boolean netting;

    @OneToMany(mappedBy = "clientPreference", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<FundGroup> fundMapping;

    @Column(name="status", nullable = false)
    private Status status;

    public ClientPrefKey getKey() {
        return new ClientPrefKey(this.clientName, this.currency);
    }

    public static List<String> getFormattedKeys(List <ClientPrefKey> keys) {
        return keys.stream()
                .map(key -> String.format("('%s','%s')", key.clientName(), key.currency().name()))
                .toList();
    }
}