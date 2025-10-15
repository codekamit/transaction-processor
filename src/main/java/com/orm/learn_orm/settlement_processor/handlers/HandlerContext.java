package com.orm.learn_orm.settlement_processor.handlers;

import com.orm.learn_orm.enums.SettlementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HandlerContext {
    private SettlementType settlementType;
}
