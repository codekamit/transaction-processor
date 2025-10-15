package com.orm.learn_orm.settlement_processor.processor;

import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.mapper.IClientPrefMapper;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.IClientPreferenceRepo;
import com.orm.learn_orm.settlement_processor.handler_lambda.HandlerLamdas;
import com.orm.learn_orm.settlement_processor.handlers.EarningHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Log4j2
@Service
@AllArgsConstructor
public class EarningProcessor implements ISettlementProcessor<Earning> {

    private static final IClientPrefMapper PREF_MAPPER = IClientPrefMapper.INSTANCE;

    private final IClientPreferenceRepo clientPreferenceRepo;
    private final HandlerLamdas lamdas;

    @Override
    @Transactional(transactionManager = "ormTransactionManager")
    public void processSettlement(List<Earning> earnings) {
        List<ClientPrefKey> keys = new ArrayList<>();
        SettlementUpload settlementUpload = earnings.get(0).getSettlementUpload();
        Map<ClientPrefKey, List<Earning>> earningsByClientPref = earnings.stream()
                .peek(earning -> keys.add(new ClientPrefKey(earning.getClientName(), earning.getCurrency())))
                .collect(groupingBy(earning -> new ClientPrefKey(earning.getClientName(), earning.getCurrency())));

        List<ClientPreference> clientPreferences = clientPreferenceRepo.findPrefWithFundMappingForClientKeys(ClientPreference.getFormattedKeys(keys));
        Map<ClientPrefKey, ClientPrefProcessingDTO> clientPrefMap = clientPreferences.stream()
                .map(PREF_MAPPER ::mapClientPrefProcessingDTO)
                .collect(Collectors.toMap(ClientPrefProcessingDTO::getKey,
                        pref -> pref));

        earningsByClientPref.forEach((clientPrefKey, clientEarnings) -> {
            EarningHandlerContext context = EarningHandlerContext.builder()
                    .clientPreference(clientPrefMap.getOrDefault(clientPrefKey, null))
                    .earnings(clientEarnings)
                    .netEarnings(new ArrayList<>())
                    .settlementUpload(settlementUpload)
                    .build();

            lamdas.fetchEarningNettingHandler().process(context);
            try {
                lamdas.fetchEarningPersistentHandler().process((context));
            } catch (UnexpectedRollbackException ex) {
                log.error(ex.getMostSpecificCause());
                log.info(ex.getRootCause());
            }
        });
    }
}
