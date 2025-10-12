package com.orm.learn_orm.service;

import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SettlementUploadService {

    private final ISettlementUploadRepo settlementUploadRepo;

    @Transactional(transactionManager = "ormTransactionManager")
    public SettlementUpload addSettlementUpload(SettlementUpload settlementUpload) {
        return settlementUploadRepo.save(settlementUpload);
    }
}
