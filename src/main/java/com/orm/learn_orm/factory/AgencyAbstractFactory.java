package com.orm.learn_orm.factory;

import com.orm.learn_orm.enums.SettlementType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AgencyAbstractFactory {

    private final EarningFactory earningFactory;
    private final BillingFactory billingFactory;


    public AgencyFactory<?, ?> getFactory(SettlementType type) {
        return switch (type) {
            case EARNING -> earningFactory;
            case BILLING -> billingFactory;
            default -> throw new IllegalArgumentException("Unknown factory type: " + type);
        };
    }
}
