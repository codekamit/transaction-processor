package com.orm.learn_orm.dto;

import com.orm.learn_orm.marker_interface.ISettlementProcessDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BillingProcessDTO implements ISettlementProcessDTO {
}
