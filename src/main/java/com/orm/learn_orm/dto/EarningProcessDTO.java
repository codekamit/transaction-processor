package com.orm.learn_orm.dto;

import com.orm.learn_orm.marker_interface.ISettlementProcessDTO;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.SettlementUpload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EarningProcessDTO implements ISettlementProcessDTO {
    List<Earning> earnings;
    List<ClientPreference> clientPreferences;
    SettlementUpload settlementUpload;
}
