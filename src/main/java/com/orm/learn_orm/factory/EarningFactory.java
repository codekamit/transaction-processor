package com.orm.learn_orm.factory;

import com.orm.learn_orm.dto.EarningDTO;
import com.orm.learn_orm.file_management.header.EarningFileHeader;
import com.orm.learn_orm.file_management.parser.EarningFileParser;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.service.EarningSettlementService;
import com.orm.learn_orm.settlement_processor.preprocessor.EarningSettlementPreprocessor;
import com.orm.learn_orm.settlement_processor.processor.EarningProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EarningFactory implements AgencyFactory<EarningDTO, Earning> {

    private final EarningFileParser fileParser;
    private final EarningFileHeader header;
    private final EarningSettlementPreprocessor preprocessor;
    private final EarningProcessor processor;
    private final EarningSettlementService earningSettlementService;

    @Override
    public EarningFileParser getParser() {
        return this.fileParser;
    }

    @Override
    public EarningFileHeader getFileHeader() {
        return this.header;
    }

    @Override
    public EarningSettlementPreprocessor getPreprocessor() {
        return this.preprocessor;
    }

    @Override
    public EarningProcessor getProcessor() { return this.processor; }

    @Override
    public EarningSettlementService getSettlementService() {
        return this.earningSettlementService;
    }


}
