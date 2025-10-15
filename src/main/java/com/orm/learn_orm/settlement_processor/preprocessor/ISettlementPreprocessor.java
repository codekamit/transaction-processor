package com.orm.learn_orm.settlement_processor.preprocessor;

import com.orm.learn_orm.factory.AgencyFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ISettlementPreprocessor {

    void preprocess(MultipartFile file, AgencyFactory<?,?> agencyFactory) throws IOException;
}
