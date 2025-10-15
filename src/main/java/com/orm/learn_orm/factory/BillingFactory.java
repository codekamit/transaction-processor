package com.orm.learn_orm.factory;

import com.orm.learn_orm.dto.BillingDTO;
import com.orm.learn_orm.file_management.header.BillingFileHeader;
import com.orm.learn_orm.file_management.parser.BillingFileParser;
import com.orm.learn_orm.model.Billing;
import com.orm.learn_orm.service.BillingSettlementService;
import com.orm.learn_orm.settlement_processor.preprocessor.BillingSettlementPreprocessor;
import com.orm.learn_orm.settlement_processor.processor.BillingProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillingFactory implements AgencyFactory<BillingDTO, Billing> {

    private final BillingFileParser fileParser;
    private final BillingFileHeader header;
    private final BillingSettlementPreprocessor preprocessor;
    private final BillingProcessor billingProcessor;
    private final BillingSettlementService billingSettlementService;


    @Override
    public BillingFileParser getParser() {
        return this.fileParser;
    }

    @Override
    public BillingFileHeader getFileHeader() {
        return this.header;
    }

    @Override
    public BillingSettlementPreprocessor getPreprocessor() {
        return this.preprocessor;
    }

    @Override
    public BillingProcessor getProcessor() {
        return this.billingProcessor;
    }

    @Override
    public BillingSettlementService getSettlementService() {
        return this.billingSettlementService;
    }
}
