package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.repo.IClientPreferenceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@AllArgsConstructor
public class EarningProcessor implements ISettlementProcessor<Earning> {

    private final IClientPreferenceRepo clientPreferenceRepo;

    @Override
    @Transactional(transactionManager = "ormTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void processSettlement(List<Earning> earnings) {
        List<ClientPrefKey> keys = new ArrayList<>();

        Map<ClientPrefKey, List<Earning>> earningsByClientPref = earnings.stream()
                .peek(earning -> keys.add(new ClientPrefKey(earning.getClientName(), earning.getCurrency())))
                .collect(groupingBy(earning -> new ClientPrefKey(earning.getClientName(), earning.getCurrency())));

        List<String> formattedKeys = ClientPreference.getFormattedKeys(keys);

        List<ClientPreference> clientPreferences = clientPreferenceRepo.findPrefWithFundMappingForClientKeys(formattedKeys);

    }
}
