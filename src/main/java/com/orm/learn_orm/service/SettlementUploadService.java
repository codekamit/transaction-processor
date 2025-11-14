package com.orm.learn_orm.service;

import com.orm.learn_orm.enums.UploadStatus;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class SettlementUploadService {

    private final ISettlementUploadRepo settlementUploadRepo;

    @Transactional(transactionManager = "ormTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SettlementUpload persistAsync(SettlementUpload settlementUpload) {
        return settlementUploadRepo.save(settlementUpload);
    }

    @Transactional(transactionManager = "ormTransactionManager", rollbackFor = Exception.class)
    public SettlementUpload persist(SettlementUpload settlementUpload) {
        return settlementUploadRepo.save(settlementUpload);
    }

    /**
     * Safely updates the status of a SettlementUpload in a new transaction
     * by fetching the latest version first. This prevents optimistic locking exceptions.
     * @param uploadId The ID of the upload to update.
     * @param status The new status to set.
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUploadStatus(UUID uploadId, UploadStatus status) {
        Optional<SettlementUpload> uploadOpt = settlementUploadRepo.findById(uploadId);

        if (uploadOpt.isPresent()) {
            SettlementUpload managedUpload = uploadOpt.get();
            managedUpload.setUploadStatus(status);
        } else {
            log.warn("Could not find SettlementUpload with ID {} to update status to {}.", uploadId, status);
        }
    }
}
