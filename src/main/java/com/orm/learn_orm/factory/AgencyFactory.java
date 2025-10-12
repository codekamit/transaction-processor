package com.orm.learn_orm.factory;

import com.orm.learn_orm.file_management.header.IFileHeader;
import com.orm.learn_orm.file_management.parser.IFileParser;
import com.orm.learn_orm.settlement_processor.IPreprocessor;

public interface AgencyFactory<T> {

    IFileParser<T> getParser();
    IFileHeader getFileHeader();
    IPreprocessor getPreprocessor();
}
