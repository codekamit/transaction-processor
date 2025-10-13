package com.orm.learn_orm.repo;

import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.enums.Currency;
import com.orm.learn_orm.model.ClientPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IClientPreferenceRepo extends JpaRepository<ClientPreference, Long> {

    @Query("SELECT DISTINCT cp FROM ClientPreference cp LEFT JOIN FETCH cp.fundMapping WHERE cp.clientName = :clientName AND cp.currency = :currency")
    Optional<ClientPreference> findPrefWithFundMapping(
            @Param("clientName") String clientName,
            @Param("currency") Currency currency
    );

    @Query("SELECT DISTINCT cp FROM ClientPreference cp LEFT JOIN FETCH cp.fundMapping")
    List<ClientPreference> findAllPrefWithMapping();

    Optional<ClientPreference> findByClientNameAndCurrency(String clientName, Currency currency);

    List<ClientPreference> findByClientNameAndCurrencyIn(String clientName, List<Currency> currencies);

    @Query("SELECT DISTINCT cp FROM ClientPreference cp LEFT JOIN FETCH cp.fundMapping WHERE cp.clientName IN :clientNames AND cp.currency IN :currencies")
    List<ClientPreference> findPrefWithMappingJQL(
            @Param("clientNames") List<String> clientNames,
            @Param("currencies") List<Currency> currencies
    );

    @Query(value = "SELECT DISTINCT cp.* FROM abn_proxy.client_preference cp " +
            "LEFT JOIN abn_proxy.fund_mapping fm ON cp.id = fm.client_preference_id " +
            "WHERE (cp.client_name, cp.currency) IN (:clientKeys)",
            nativeQuery = true)
    List<ClientPreference> findPrefWithFundMappingForClientKeys(
            @Param("clientKeys") List<String> clientKeys
    );
}
