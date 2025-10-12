package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.enums.SettlementType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SettlementProcessor {

    public void processSettlement(MultipartFile file, SettlementType type) {
    }
}
