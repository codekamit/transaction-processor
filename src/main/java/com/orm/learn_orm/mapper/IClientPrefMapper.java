package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.dto.FundGroupDTO;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.FundGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface IClientPrefMapper {

    IClientPrefMapper INSTANCE = Mappers.getMapper(IClientPrefMapper.class);

    @Mapping(target = "fundMapping", ignore = true)
    @Mapping(target = "status", ignore = true)
    ClientPreference getClientPreference(ClientPreferenceDTO clientPreferenceDTO);

    @Mapping(target = "fundGroup", ignore = true)
    ClientPreferenceDTO getClientPreferenceDTO(ClientPreference clientPreference);

    void updateClientPreference(ClientPreferenceDTO clientPreferenceDTO, @MappingTarget ClientPreference clientPreference);

    default List<FundGroup> mapFundGroup(FundGroupDTO dto, ClientPreference clientPreference) {
        if (dto == null) {
            return null;
        }
        List<FundGroup> fundGroups = new ArrayList<>();
        dto.getFundMap().forEach((fund, paymentFund) -> {
            FundGroup fundGroup = new FundGroup();
            fundGroup.setFund(fund);
            fundGroup.setPaymentFund(paymentFund);
            fundGroup.setClientPreference(clientPreference);
            fundGroups.add(fundGroup);
        });
        return fundGroups;
    }

    default FundGroupDTO mapFundGroupDTO(ClientPreference clientPreference) {
        if (clientPreference.getFundMapping() == null) {
            return null;
        }
        FundGroupDTO fundGroupDTO = new FundGroupDTO();
        Map<String, String> mapping = new HashMap<>();
        clientPreference.getFundMapping().forEach(fundGroup -> {
            mapping.put(fundGroup.getFund(), fundGroup.getPaymentFund());
        });
        fundGroupDTO.setFundMap(mapping);
        return fundGroupDTO;
    }

    default ClientPrefProcessingDTO mapClientPrefProcessingDTO(ClientPreference clientPreference) {
        ClientPrefProcessingDTO clientPrefProcessingDTO = new ClientPrefProcessingDTO();
        if (SettlementLevel.CLIENT == clientPreference.getSettlementLevel()) {
            String paymentFund = clientPreference.getFundMapping()
                    .get(0)
                    .getPaymentFund();
            clientPrefProcessingDTO.setPaymentFund(paymentFund);
            clientPrefProcessingDTO.setFundMapping(null);
        } else if (SettlementLevel.PAYMENT_FUND == clientPreference.getSettlementLevel()) {
            Map<String, String> mapping = clientPreference.getFundMapping().stream()
                    .collect(Collectors.toMap(
                            FundGroup::getFund,
                            FundGroup::getPaymentFund));
            clientPrefProcessingDTO.setFundMapping(mapping);
            clientPrefProcessingDTO.setPaymentFund(null);
        } else {
            clientPrefProcessingDTO.setPaymentFund(null);
            clientPrefProcessingDTO.setFundMapping(new HashMap<>());
        }
        clientPrefProcessingDTO.setClientName(clientPreference.getClientName());
        clientPrefProcessingDTO.setCurrency(clientPreference.getCurrency());
        clientPrefProcessingDTO.setNetting(clientPreference.isNetting());
        clientPrefProcessingDTO.setStatus(clientPreference.getStatus());
        clientPrefProcessingDTO.setSettlementLevel(clientPreference.getSettlementLevel());
        return clientPrefProcessingDTO;
    }
}
