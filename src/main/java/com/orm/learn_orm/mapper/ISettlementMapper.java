package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.BillingDTO;
import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.dto.EarningDTO;
import com.orm.learn_orm.model.Billing;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

@Mapper(imports = {ArrayList.class})
public interface ISettlementMapper {

    ISettlementMapper INSTANCE = Mappers.getMapper(ISettlementMapper.class);

    EarningDTO getEarningDTO(Earning earning);

    @Mapping(target = "state", constant = "PENDING")
    Earning getEarning(EarningDTO earningDTO);

    BillingDTO getBillingDTO(Billing billing);

    Billing getBilling(BillingDTO billingDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", constant = "PROCESSED_WITH_DEFAULT_PREF_MISSING")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "clientName", source = "clientName")
    @Mapping(target = "paymentFund", source = "fund")
    @Mapping(target = "settlementLevel", constant = "FUND")
    @Mapping(target = "settlementType", constant = "EARNING")
    @Mapping(target = "lastLinkedTo", expression = "java(new ArrayList<>())")
    @Mapping(target = "currency", source = "currency")
    NetEarning getNetEarningForMissingPref(Earning earning);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "paymentFund", ignore = true)
    @Mapping(target = "clientName", source = "clientPreference.clientName")
    @Mapping(target = "settlementLevel", source = "clientPreference.settlementLevel")
    @Mapping(target = "settlementType", constant = "EARNING")
    @Mapping(target = "lastLinkedTo", expression = "java(new ArrayList<>())")
    @Mapping(target = "currency", source = "clientPreference.currency")
    NetEarning getNetEarningFromRefEarning(Earning earning, ClientPrefProcessingDTO clientPreference);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", constant = "PROCESSED_WITH_DEFAULT_FUND_GROUP_MISSING")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "clientName", source = "clientName")
    @Mapping(target = "paymentFund", source = "fund")
    @Mapping(target = "settlementLevel", constant = "FUND")
    @Mapping(target = "settlementType", constant = "EARNING")
    @Mapping(target = "lastLinkedTo", expression = "java(new ArrayList<>())")
    @Mapping(target = "currency", source = "currency")
    NetEarning getNetEarningForMissingFundMap(Earning earning);
}
