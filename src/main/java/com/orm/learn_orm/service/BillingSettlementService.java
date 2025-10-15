package com.orm.learn_orm.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class BillingSettlementService implements ISettlementService {

    @Override
    public void suspendNettedSettlement(UUID id) {

    }

    @Override
    public void reprocessDefaultPreference() {

    }
}
