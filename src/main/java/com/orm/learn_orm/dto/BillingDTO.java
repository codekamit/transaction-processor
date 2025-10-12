package com.orm.learn_orm.dto;

import com.orm.learn_orm.enums.Currency;
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
public class BillingDTO {

    private Double receivedAmount;
    private String currency;
    private String brokerNumber;
    private String brokerShortname;

    public boolean isValid(Map<String, String> errorMap) {
        boolean isValid = true;
        if (!Currency.activeCurrencySet.contains(Currency.valueOf(currency))) {
            errorMap.put(currency, "Currency is not valid");
            isValid = false;
        }
        if (StringUtils.isBlank(brokerShortname)) {
            errorMap.put(brokerShortname, "Broker ShortName cannot be blank");
            isValid = false;
        }
        if (StringUtils.isBlank(brokerNumber)) {
            errorMap.put(brokerNumber, "Broker Number cannot be blank");
            isValid = false;
        }
        return isValid;
    }
}
