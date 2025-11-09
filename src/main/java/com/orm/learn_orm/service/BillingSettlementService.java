package com.orm.learn_orm.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillingSettlementService implements ISettlementService {

    @Override
    public void suspendNettedSettlement(String id) {

    }

    @Override
    public void reprocessDefaultPreference() {

    }
}
