package com.orm.learn_orm.service;

import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.mapper.IClientPrefMapper;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.FundGroup;
import com.orm.learn_orm.repo.IClientPreferenceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientPreferenceService {

    private static final IClientPrefMapper CLIENT_PREF_MAPPER = IClientPrefMapper.INSTANCE;

    private final IClientPreferenceRepo clientPreferenceRepo;

    @Transactional(transactionManager = "ormTransactionManager")
    public void createPreference(ClientPreferenceDTO dto) {
        ClientPreference clientPreference = CLIENT_PREF_MAPPER.getClientPreference(dto);
        List<FundGroup> fundGroups = dto.getFundMapping().stream()
                .map(CLIENT_PREF_MAPPER::getFundGroup)
                .peek(fundGroup -> fundGroup.setClientPreference(clientPreference))
                .toList();
        clientPreference.setFundMapping(fundGroups);
        clientPreferenceRepo.save(clientPreference);
    }

    @Transactional(readOnly = true, transactionManager = "ormTransactionManager")
    public ClientPreferenceDTO getPreferenceByBusinessKey(String clientName, Currency currency) {
        ClientPreference clientPreference = clientPreferenceRepo.findByClientNameAndCurrency(clientName,currency)
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        return CLIENT_PREF_MAPPER.getClientPreferenceDTO(clientPreference);
    }

    @Transactional(readOnly = true, transactionManager = "ormTransactionManager")
    public List<ClientPreferenceDTO> getAllPreferences() {
        List<ClientPreference> clientPreferences = clientPreferenceRepo.findAllPrefWithMapping();
        return clientPreferences.stream()
                .map(CLIENT_PREF_MAPPER::getClientPreferenceDTO)
                .toList();
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void updatePreference(Long id, ClientPreferenceDTO dto) {
        ClientPreference clientPreference = clientPreferenceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        CLIENT_PREF_MAPPER.updateClientPreference(dto, clientPreference);
        clientPreferenceRepo.save(clientPreference);
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void deletePreference(Long id) {
        ClientPreference clientPreference = clientPreferenceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        clientPreferenceRepo.delete(clientPreference);
    }

}
