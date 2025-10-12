package com.orm.learn_orm.dto;

import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.SettlementLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClientPreferenceDTO {
    private String clientName;
    private SettlementLevel settlementLevel;
    private Currency currency;
    private boolean netting;
    private List<FundGroupDTO> fundMapping;
}
