package com.orm.learn_orm.factory;

import com.orm.learn_orm.file_management.header.EarningFileHeader;
import com.orm.learn_orm.file_management.parser.EarningFileParser;
import com.orm.learn_orm.settlement_processor.EarningPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EarningFactory {

    private final EarningFileParser fileParser;
    private final EarningFileHeader header;
    private final EarningPreprocessor preprocessor;
}
