package com.orm.learn_orm.factory;

import com.orm.learn_orm.file_management.header.BillingFileHeader;
import com.orm.learn_orm.file_management.parser.BillingFileParser;
import com.orm.learn_orm.settlement_processor.BillingPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillingFactory {

    private final BillingFileParser fileParser;
    private final BillingFileHeader header;
    private final BillingPreprocessor preprocessor;
}
