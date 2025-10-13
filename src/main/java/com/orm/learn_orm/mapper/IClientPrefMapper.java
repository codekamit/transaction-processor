package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.dto.FundGroupDTO;
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
}
