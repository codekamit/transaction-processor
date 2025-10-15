package com.orm.learn_orm.settlement_processor.processor;

import com.orm.learn_orm.marker_interface.ISettlement;

import java.util.List;

public interface ISettlementProcessor<E extends ISettlement> {

    void processSettlement(List<E> settlements);
}
