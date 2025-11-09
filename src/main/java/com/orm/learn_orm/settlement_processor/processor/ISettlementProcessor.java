package com.orm.learn_orm.settlement_processor.processor;

import com.orm.learn_orm.marker_interface.ISettlement;
import com.orm.learn_orm.marker_interface.ISettlementNetted;
import com.orm.learn_orm.marker_interface.ISettlementProcessDTO;
import com.orm.learn_orm.model.NetEarning;

import java.util.List;


public interface ISettlementProcessor<S extends ISettlementProcessDTO, E extends ISettlementNetted> {

    List<E> processSettlement(S settlementProcessDTO);
}
