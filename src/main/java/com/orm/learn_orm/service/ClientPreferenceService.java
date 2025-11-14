package com.orm.learn_orm.service;

import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.dto.ClientPreferenceDTO;
import com.orm.learn_orm.dto.FundGroupDTO;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.enums.Status;
import com.orm.learn_orm.mapper.IClientPrefMapper;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.FundGroup;
import com.orm.learn_orm.repo.IClientPreferenceRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


@Log4j2
@Service
@AllArgsConstructor
public class ClientPreferenceService {

    private static final IClientPrefMapper CLIENT_PREF_MAPPER = IClientPrefMapper.INSTANCE;

    private final IClientPreferenceRepo clientPreferenceRepo;

    @Transactional(transactionManager = "ormTransactionManager")
    public void createPreference(ClientPreferenceDTO dto) {
        ClientPreference clientPreference = CLIENT_PREF_MAPPER.getClientPreference(dto);
        List<FundGroup> fundGroups = CLIENT_PREF_MAPPER.mapFundGroup(dto.getFundGroup(), clientPreference);
        clientPreference.setFundMapping(fundGroups);
        clientPreference.setStatus(Status.ACTIVE);
        clientPreferenceRepo.save(clientPreference);
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void createPreferences(List<ClientPreferenceDTO> dtos) {
        List<ClientPreference> clientPreferences = dtos.stream()
                .map(dto -> {
                    ClientPreference clientPreference = CLIENT_PREF_MAPPER.getClientPreference(dto);
                    List<FundGroup> fundGroups = CLIENT_PREF_MAPPER.mapFundGroup(dto.getFundGroup(), clientPreference);
                    clientPreference.setFundMapping(fundGroups);
                    clientPreference.setStatus(Status.ACTIVE);
                    return clientPreference;
                }).toList();

        clientPreferenceRepo.saveAll(clientPreferences);
    }

    @Transactional(readOnly = true, transactionManager = "ormTransactionManager")
    public ClientPreferenceDTO getPreferenceByBusinessKey(String clientName, Currency currency) {
        ClientPreference clientPreference = clientPreferenceRepo.findByClientNameAndCurrency(clientName, currency)
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        return CLIENT_PREF_MAPPER.getClientPreferenceDTO(clientPreference);
    }

    @Transactional(readOnly = true, transactionManager = "ormTransactionManager")
    public List<ClientPreferenceDTO> getPreferencesByKeys(List<ClientPrefKey> keys) {
        List<ClientPreference> clientPreferences = clientPreferenceRepo
                .findPrefWithFundMappingForClientKeys(ClientPreference.getFormattedKeys(keys));
        return clientPreferences.stream()
                .map(clientPreference -> {
                    ClientPreferenceDTO clientPreferenceDTO = CLIENT_PREF_MAPPER.getClientPreferenceDTO(clientPreference);
                    FundGroupDTO fundGroupDTO = CLIENT_PREF_MAPPER.mapFundGroupDTO(clientPreference);
                    clientPreferenceDTO.setFundGroup(fundGroupDTO);
                    return clientPreferenceDTO;
                }).toList();
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void updateClientPreference(ClientPreferenceDTO dto) {
        ClientPreference clientPreference = clientPreferenceRepo.findByClientNameAndCurrency(dto.getClientName(), dto.getCurrency())
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        CLIENT_PREF_MAPPER.updateClientPreference(dto, clientPreference);

        List<FundGroup> newFundGroups = CLIENT_PREF_MAPPER.mapFundGroup(dto.getFundGroup(), clientPreference);
        clientPreference.getFundMapping().clear();
        clientPreference.getFundMapping().addAll(newFundGroups == null ? new ArrayList<FundGroup>() : newFundGroups);
        clientPreferenceRepo.save(clientPreference);
    }

    @Transactional(readOnly = true, transactionManager = "ormTransactionManager")
    public List<ClientPreferenceDTO> getAllPreferences() {
        List<ClientPreference> clientPreferences = clientPreferenceRepo.findAllPrefWithMapping();
        return clientPreferences.stream()
                .map(clientPref -> {
                    ClientPreferenceDTO clientPreferenceDTO = CLIENT_PREF_MAPPER.getClientPreferenceDTO(clientPref);
                    FundGroupDTO fundGroupDTO = CLIENT_PREF_MAPPER.mapFundGroupDTO(clientPref);
                    clientPreferenceDTO.setFundGroup(fundGroupDTO);
                    return clientPreferenceDTO;
                }).toList();
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void updatePreference(UUID id, ClientPreferenceDTO dto) {
        ClientPreference clientPreference = clientPreferenceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        CLIENT_PREF_MAPPER.updateClientPreference(dto, clientPreference);
        clientPreferenceRepo.save(clientPreference);
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void deletePreference(UUID id) {
        ClientPreference clientPreference = clientPreferenceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientPreference not found"));
        clientPreferenceRepo.delete(clientPreference);
    }

    @Retryable(
            value = { OptimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional(transactionManager = "ormTransactionManager")
    public void hitGo() throws Throwable {
        try {

            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                log.info("Created by thread : " + Thread.currentThread().getName());
                ClientPreference clientPreference = clientPreferenceRepo.findByClientNameAndCurrency("AMIT", Currency.CAD).get();
                clientPreference.setClientName("AMIT");
                clientPreference.setFundMapping(null);
                clientPreference.setNetting(false);
                clientPreference.setCurrency(Currency.CAD);
                clientPreference.setSettlementLevel(SettlementLevel.CLIENT);
                if (clientPreference.getStatus() == Status.INACTIVE) {
                    clientPreference.setStatus(Status.ACTIVE);
                } else {
                    throw new RuntimeException("Client preference is already inactive. THREAD : "
                            +Thread.currentThread().getName());
                }
                clientPreferenceRepo.save(clientPreference);
            });

            CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
                log.info("Created by thread : " + Thread.currentThread().getName());
                ClientPreference clientPreference = clientPreferenceRepo.findByClientNameAndCurrency("AMIT", Currency.CAD).get();
                clientPreference.setClientName("AMIT");
                clientPreference.setFundMapping(null);
                clientPreference.setNetting(false);
                clientPreference.setCurrency(Currency.CAD);
                clientPreference.setSettlementLevel(SettlementLevel.CLIENT);
                if (clientPreference.getStatus() == Status.INACTIVE) {
                    clientPreference.setStatus(Status.ACTIVE);
                } else {
                    throw new RuntimeException("Client preference is already inactive. THREAD : "
                            +Thread.currentThread().getName());
                }
                clientPreferenceRepo.save(clientPreference);
            });

            future1.join();
            future2.join();

        } catch (CompletionException ex) {
            log.info(ex.getCause().getMessage());
            throw ex.getCause();
        }
    }

}