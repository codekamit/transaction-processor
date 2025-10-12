package com.orm.learn_orm.file_management.header;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingFileHeader implements IFileHeader {

    private final int HEADER_START_ROW = 1;
    private final int HEADER_START_COL = 0;
    private final int FOOTER_COL = 0;
    private final int SHEET_NUMBER = 1;
    private final String FOOTER_TEXT = "END OF BILLING FILE";
    private final String BROKER_SHORTNAME = "Broker Shortname";
    private final String BROKER_NUMBER = "Broker Number";
    private final String CURRENCY = "Currency";
    private final String RECEIVED_AMOUNT = "Received Amount";

    @Override
    public int headerStartRow() {
        return this.HEADER_START_ROW;
    }

    @Override
    public int headerStartCol() {
        return this.HEADER_START_COL;
    }

    @Override
    public int footerCol() {
        return this.FOOTER_COL;
    }

    @Override
    public int sheetNumber() {
        return this.SHEET_NUMBER;
    }

    @Override
    public String footerText() {
        return this.FOOTER_TEXT;
    }

    @Override
    public List<String> headers() {
        return List.of(BROKER_SHORTNAME, BROKER_NUMBER, CURRENCY, RECEIVED_AMOUNT);
    }

    public String BROKER_SHORTNAME() {
        return BROKER_SHORTNAME;
    }

    public String BROKER_NUMBER() {
        return BROKER_NUMBER;
    }

    public String CURRENCY() {
        return CURRENCY;
    }

    public String RECEIVED_AMOUNT() {
        return RECEIVED_AMOUNT;
    }
}
