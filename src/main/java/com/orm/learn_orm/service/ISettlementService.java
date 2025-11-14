package com.orm.learn_orm.service;

public interface ISettlementService {

    void suspendNettedSettlement(String id);

    void reprocessDefaultPreference();
}
