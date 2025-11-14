package com.orm.learn_orm.service;

import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.dto.EarningProcessDTO;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import com.orm.learn_orm.repo.IClientPreferenceRepo;
import com.orm.learn_orm.repo.IEarningRepo;
import com.orm.learn_orm.repo.INetEarningRepo;
import com.orm.learn_orm.settlement_processor.processor.EarningProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class EarningSettlementService implements ISettlementService {

    private final IEarningRepo earningRepo;
    private final IClientPreferenceRepo clientPreferenceRepo;
    private final INetEarningRepo netEarningRepo;
    private final EarningProcessor earningProcessor;

    @Override
    public void suspendNettedSettlement(String id) {
        NetEarning netEarning = netEarningRepo.findNetEarningWithChild(id)
                .orElseThrow(() -> new RuntimeException("NetEarning not found"));
        netEarningRepo.save(netEarning);
    }

    @Override
    public void reprocessDefaultPreference() {
        reprocessOrphanedEarnings();
    }

    public void reprocessOrphanedEarnings() {
        List<Earning> orphanedEarnings = earningRepo.findAllOrphanedEarnings();

        List<ClientPrefKey> keys = new ArrayList<>();
        orphanedEarnings.forEach(earning -> {
            keys.add(new ClientPrefKey(earning.getClientName(), earning.getCurrency()));
        });
        List<ClientPreference> clientPreferences = clientPreferenceRepo.findPrefWithFundMappingForClientKeys(ClientPreference.getFormattedKeys(keys));

        EarningProcessDTO earningProcessDTO = EarningProcessDTO.builder()
                .earnings(orphanedEarnings)
                .clientPreferences(clientPreferences)
                .build();
        earningProcessor.processSettlement(earningProcessDTO);
    }
}
