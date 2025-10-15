package com.orm.learn_orm.dto;

import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClientPrefProcessingDTO {
    private String clientName;
    private SettlementLevel settlementLevel;
    private Currency currency;
    private boolean netting;
    private String paymentFund;
    private Map<String,String> fundMapping;
    private Status status;

    public ClientPrefKey getKey() {
        return new ClientPrefKey(this.clientName, this.currency);
    }
}
