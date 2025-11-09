package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.factory.AgencyAbstractFactory;
import com.orm.learn_orm.factory.AgencyFactory;
import com.orm.learn_orm.service.ISettlementService;
import com.orm.learn_orm.settlement_processor.preprocessor.ISettlementPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SettlementProcessor {

    private final AgencyAbstractFactory agencyAbstractFactory;

    public void processSettlement(MultipartFile file, SettlementType type) throws IOException {
        AgencyFactory<?,?,?,?> agencyFactory = agencyAbstractFactory.getFactory(type);
        ISettlementPreprocessor preprocessor = agencyFactory.getPreprocessor();
        preprocessor.preprocess(file, agencyFactory);
    }

    public void suspendNettedSettlement(String id, SettlementType settlementType) {
        AgencyFactory<?,?,?,?> agencyFactory = agencyAbstractFactory.getFactory(settlementType);
        ISettlementService settlementService = agencyFactory.getSettlementService();
        settlementService.suspendNettedSettlement(id);
    }

    public void reprocessDefaultPreference(SettlementType settlementType) {
        AgencyFactory<?,?,?,?> agencyFactory = agencyAbstractFactory.getFactory(settlementType);
        ISettlementService settlementService = agencyFactory.getSettlementService();
        settlementService.reprocessDefaultPreference();
    }
}
