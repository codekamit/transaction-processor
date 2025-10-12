package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.BillingDTO;
import com.orm.learn_orm.dto.EarningDTO;
import com.orm.learn_orm.model.Billing;
import com.orm.learn_orm.model.Earning;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ISettlementMapper {

    ISettlementMapper INSTANCE = Mappers.getMapper(ISettlementMapper.class);

    EarningDTO getEarningDTO(Earning earning);
    Earning getEarning(EarningDTO earningDTO);

    BillingDTO getBillingDTO(Billing billing);
    Billing getBilling(BillingDTO billingDTO);
}
