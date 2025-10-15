package com.orm.learn_orm.service;

import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import com.orm.learn_orm.repo.IEarningRepo;
import com.orm.learn_orm.repo.INetEarningRepo;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import com.orm.learn_orm.settlement_processor.processor.EarningProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class EarningSettlementService implements ISettlementService {

    private final IEarningRepo earningRepo;
    private final ISettlementUploadRepo settlementUploadRepo;
    private final INetEarningRepo netEarningRepo;
    private final EarningProcessor earningProcessor;

    @Override
    public void suspendNettedSettlement(UUID id) {
        NetEarning netEarning = netEarningRepo.findNetEarningWithChild(id)
                .orElseThrow(() -> new RuntimeException("NetEarning not found"));
        netEarning.unlinkEarnings();
        netEarningRepo.save(netEarning);
    }

    @Override
    public void reprocessDefaultPreference() {
        reprocessOrphanedEarnings();
    }

    public void reprocessOrphanedEarnings() {
        List<Earning> orphanedEarnings = earningRepo.findAllOrphanedEarnings();
        earningProcessor.processSettlement(orphanedEarnings);
    }
}
