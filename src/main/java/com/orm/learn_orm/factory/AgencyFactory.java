package com.orm.learn_orm.factory;

import com.orm.learn_orm.file_management.header.IFileHeader;
import com.orm.learn_orm.file_management.parser.IFileParser;
import com.orm.learn_orm.marker_interface.ISettlement;
import com.orm.learn_orm.marker_interface.ISettlementDTO;
import com.orm.learn_orm.marker_interface.ISettlementNetted;
import com.orm.learn_orm.marker_interface.ISettlementProcessDTO;
import com.orm.learn_orm.service.ISettlementService;
import com.orm.learn_orm.settlement_processor.preprocessor.ISettlementPreprocessor;
import com.orm.learn_orm.settlement_processor.processor.ISettlementProcessor;

public interface AgencyFactory<T extends ISettlementDTO, E extends ISettlement, S extends ISettlementProcessDTO, R extends ISettlementNetted> {

    IFileParser<T> getParser();

    IFileHeader getFileHeader();

    ISettlementPreprocessor getPreprocessor();

    ISettlementProcessor<S, R> getProcessor();

    ISettlementService getSettlementService();

    ISettlementProcessDTO getSettlementProcessDTO();
}
