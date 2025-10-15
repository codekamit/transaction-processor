package com.orm.learn_orm.settlement_processor.processor;

import com.orm.learn_orm.model.Billing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class BillingProcessor implements ISettlementProcessor<Billing> {

    @Override
    public void processSettlement(List<Billing> settlements) {

    }
}
