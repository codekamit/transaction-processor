package com.orm.learn_orm.settlement_processor.processor;

import com.orm.learn_orm.dto.BillingProcessDTO;
import com.orm.learn_orm.model.Billing;
import com.orm.learn_orm.model.NetBilling;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class BillingProcessor implements ISettlementProcessor<BillingProcessDTO, NetBilling> {

    @Override
    public List<NetBilling> processSettlement(BillingProcessDTO billingProcessDTO) {
        return null;
    }
}
