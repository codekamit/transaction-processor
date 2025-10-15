package com.orm.learn_orm.settlement_processor.handler_lambda;


import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.IEarningRepo;
import com.orm.learn_orm.repo.INetEarningRepo;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import com.orm.learn_orm.settlement_processor.handlers.BaseHandler;
import com.orm.learn_orm.settlement_processor.handlers.EarningHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class HandlerLamdas {

    private final EarningNettingScenario earningNettingScenario;
    private final INetEarningRepo netEarningRepo;
    private final IEarningRepo earningRepo;
    private final ISettlementUploadRepo uploadRepo;


    @Transactional(transactionManager = "ormTransactionManager")
    public BaseHandler<EarningHandlerContext> fetchEarningNettingHandler() {
        return context -> {
            ClientPrefProcessingDTO clientPreference = context.getClientPreference();
            if(clientPreference == null) {
                earningNettingScenario.defaultNettingClientPrefMissing(context);
            }
            else {
                if(SettlementLevel.CLIENT == clientPreference.getSettlementLevel()) {
                    if (clientPreference.isNetting()) {
                        earningNettingScenario.nettingClientLevel(context);
                    } else {
                        earningNettingScenario.noNettingClientLevel(context);
                    }
                } else if(SettlementLevel.FUND == clientPreference.getSettlementLevel()) {
                    if (clientPreference.isNetting()) {
                        earningNettingScenario.nettingFundLevel(context);
                    } else {
                        earningNettingScenario.noNettingFundLevel(context);
                    }
                }
                else if(SettlementLevel.PAYMENT_FUND == clientPreference.getSettlementLevel()) {
                    if (clientPreference.isNetting()) {
                        earningNettingScenario.nettingPaymentFundLevel(context);
                    } else {
                        earningNettingScenario.noNettingPaymentFundLevel(context);
                    }
                }
            }

            SettlementUpload settlementUpload = context.getSettlementUpload();
            settlementUpload.setEarnings(context.getEarnings());
            settlementUpload.setNetEarnings(context.getNetEarnings());
        };
    }

    public BaseHandler<EarningHandlerContext> fetchEarningPersistentHandler() {
        return context -> {
            try {
            saveSettlements(context.getSettlementUpload());
            } catch (UnexpectedRollbackException ex) {
                log.error(ex.getMostSpecificCause());
                log.info(ex.getRootCause());
            }
        };
    }

    public void saveSettlements(SettlementUpload settlementUpload) {
        uploadRepo.save(settlementUpload);
    }
}
