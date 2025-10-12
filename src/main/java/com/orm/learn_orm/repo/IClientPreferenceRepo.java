package com.orm.learn_orm.repo;

import com.orm.learn_orm.model.ClientPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public interface IClientPreferenceRepo extends JpaRepository<ClientPreference, Long> {

    @Query("SELECT DISTINCT cp FROM ClientPreference cp LEFT JOIN FETCH cp.fundMapping WHERE cp.clientName = :clientName AND cp.currency = :currency")
    Optional<ClientPreference> findAllWithFundMapping(
            @Param("clientName") String clientName,
            @Param("currency") Currency currency
    );

    Optional<ClientPreference> findByClientNameAndCurrency(String clientName, Currency currency);

    List<ClientPreference> findByClientNameAndCurrencyIn(String clientName, List<Currency> currencies);
}
