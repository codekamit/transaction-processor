package com.orm.learn_orm.dto;


import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.marker_interface.ISettlementDTO;
import io.micrometer.common.util.StringUtils;
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
public class EarningDTO implements ISettlementDTO {

    private String currency;
    private String clientName;
    private String fund;
    private Double amount;

    public boolean isValid(Map<String, String> errorMap) {
        boolean isValid = true;
        if (!Currency.activeCurrencySet.contains(Currency.valueOf(currency))) {
            errorMap.put(currency, "Currency is not valid");
            isValid = false;
        }
        if (StringUtils.isBlank(clientName)) {
            errorMap.put(clientName, "Client Name cannot be blank");
            isValid = false;
        }
        if (StringUtils.isBlank(fund)) {
            errorMap.put(fund, "Fund Name cannot be blank");
            isValid = false;
        }
        return isValid;
    }
}
