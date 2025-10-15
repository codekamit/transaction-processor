package com.orm.learn_orm.dto;

import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.SettlementLevel;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"fundMapping", "settlementLevel", "netting"})
public class ClientPreferenceDTO {
    private String clientName;
    private SettlementLevel settlementLevel;
    private Currency currency;
    private boolean netting;
    private FundGroupDTO fundGroup;
}
