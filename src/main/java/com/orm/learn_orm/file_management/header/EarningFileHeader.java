package com.orm.learn_orm.file_management.header;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EarningFileHeader implements IFileHeader {

    private final int HEADER_START_ROW = 0;
    private final int HEADER_START_COL = 0;
    private final int FOOTER_COL = 1;
    private final int SHEET_NUMBER = 0;
    private final String FOOTER_TEXT = "END OF FILE";
    private final String CLIENT_NAME = "Client Name";
    private final String FUND = "Fund Name";
    private final String CURRENCY = "Currency";
    private final String AMOUNT = "Amount";

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
        return List.of(CLIENT_NAME, FUND, CURRENCY, AMOUNT);
    }

    public String CLIENT_NAME() {
        return CLIENT_NAME;
    }

    public String FUND() {
        return FUND;
    }

    public String CURRENCY() {
        return CURRENCY;
    }

    public String AMOUNT() {
        return AMOUNT;
    }
}
