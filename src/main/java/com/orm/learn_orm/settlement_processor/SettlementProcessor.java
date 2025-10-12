package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.enums.SettlementType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SettlementProcessor {

    private final EarningPreprocessor earningPreprocessor;
    private final BillingPreprocessor billingPreprocessor;

    public void processSettlement(MultipartFile file, SettlementType type) throws IOException {
        switch (type) {
            case BILLING -> billingPreprocessor.preprocess(file);
            case EARNING -> earningPreprocessor.preprocess(file);
        }
    }
}
