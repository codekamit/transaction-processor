package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.marker_interface.ISettlement;

import java.util.List;

public interface ISettlementProcessor<T extends ISettlement> {

    void processSettlement(List<T> settlements);
}
