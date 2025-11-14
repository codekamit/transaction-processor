package com.orm.learn_orm.my_tests.transaction_test;

import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.UploadStatus;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionTest {

    private final ISettlementUploadRepo settlementUploadRepo;

    @Transactional(transactionManager = "ormTransactionManager", rollbackFor = Exception.class)
    public void testTransactionBehavior() throws MyCheckedException {
        try {
            SettlementUpload settlementUpload = new SettlementUpload();
            settlementUpload.setUploadStatus(UploadStatus.SUCCESS);
            settlementUpload.setSettlementType(SettlementType.EARNING);
            settlementUpload.setFileName("Passed Transaction");
            settlementUpload.setFileSize("1024KB");
            settlementUploadRepo.save(settlementUpload);
            throw new MyCheckedException("CHECKED EXCEPTION THROWN");
        } catch (Exception ex) {
            SettlementUpload settlementUpload = new SettlementUpload();
            settlementUpload.setUploadStatus(UploadStatus.FAILURE);
            settlementUpload.setSettlementType(SettlementType.EARNING);
            settlementUpload.setFileName("Failed Transaction");
            settlementUpload.setFileSize("0KB");
            settlementUploadRepo.save(settlementUpload);
            throw ex;
        }
    }
}
