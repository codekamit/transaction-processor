package com.orm.learn_orm.service;

import java.util.UUID;

public interface ISettlementService {

    void suspendNettedSettlement(UUID id);

    void reprocessDefaultPreference();
}
