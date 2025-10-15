package com.orm.learn_orm.service;

import java.util.UUID;

public interface ISettlementService {

    public void suspendNettedSettlement(UUID id);
    void reprocessDefaultPreference();
}
