package com.orm.learn_orm.mapper;

import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.dto.FundGroupDTO;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.FundGroup;
import org.mapstruct.Mapper;

@Mapper
public interface IClientPrefMapper {

    ClientPreference getClientPreference(ClientPreferenceDTO clientPreferenceDTO);

    FundGroup getFundGroup(FundGroupDTO fundGroupDTO);

    ClientPreferenceDTO getClientPreferenceDTO(ClientPreference clientPreference);

    FundGroupDTO getFundGroupDTO(FundGroup fundGroup);
}
