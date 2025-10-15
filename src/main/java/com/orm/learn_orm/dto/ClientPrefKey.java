package com.orm.learn_orm.dto;

import com.orm.learn_orm.enums.Currency;

public record ClientPrefKey(String clientName, Currency currency) {}
