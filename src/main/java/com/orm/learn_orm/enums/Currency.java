package com.orm.learn_orm.enums;

import java.util.Set;

public enum Currency {
    CAD,
    USD,
    EUR,
    AUD,
    GBP,
    JPY;

    public static final Set<Currency> activeCurrencySet;

    static {
        activeCurrencySet = Set.of(values());
    }
}
