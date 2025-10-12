package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.dto.FundGroupDTO;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.FundGroup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IClientPrefMapper {

    IClientPrefMapper INSTANCE = Mappers.getMapper(IClientPrefMapper.class);

    ClientPreference getClientPreference(ClientPreferenceDTO clientPreferenceDTO);

    FundGroup getFundGroup(FundGroupDTO fundGroupDTO);

    ClientPreferenceDTO getClientPreferenceDTO(ClientPreference clientPreference);

    FundGroupDTO getFundGroupDTO(FundGroup fundGroup);

    void updateClientPreference(ClientPreferenceDTO clientPreferenceDTO, @MappingTarget ClientPreference clientPreference);
}
