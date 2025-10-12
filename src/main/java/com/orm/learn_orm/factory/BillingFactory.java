package com.orm.learn_orm.factory;

import com.orm.learn_orm.dto.BillingDTO;
import com.orm.learn_orm.file_management.header.BillingFileHeader;
import com.orm.learn_orm.file_management.parser.BillingFileParser;
import com.orm.learn_orm.settlement_processor.BillingPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillingFactory implements AgencyFactory<BillingDTO> {

    private final BillingFileParser fileParser;
    private final BillingFileHeader header;
    private final BillingPreprocessor preprocessor;


    @Override
    public BillingFileParser getParser() {
        return this.fileParser;
    }

    @Override
    public BillingFileHeader getFileHeader() {
        return this.header;
    }

    @Override
    public BillingPreprocessor getPreprocessor() {
        return this.preprocessor;
    }
}
