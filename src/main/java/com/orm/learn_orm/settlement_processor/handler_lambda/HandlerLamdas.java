package com.orm.learn_orm.settlement_processor.handler_lambda;


import com.orm.learn_orm.config.DailySequenceGenerator;
import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.enums.SettlementLevel;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import com.orm.learn_orm.settlement_processor.handlers.BaseHandler;
import com.orm.learn_orm.settlement_processor.handlers.EarningHandlerContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class HandlerLamdas {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    private final EarningNettingScenario earningNettingScenario;
    private final ISettlementUploadRepo uploadRepo;
    private final DailySequenceGenerator idGenerator;
    @PersistenceContext(name = "ormEntityManagerFactory")
    private EntityManager entityManager;


    @Transactional(transactionManager = "ormTransactionManager")
    public BaseHandler<EarningHandlerContext> fetchEarningNettingHandler() {
        return context -> {
            ClientPrefProcessingDTO clientPreference = context.getClientPreference();
            if (clientPreference == null) {
                earningNettingScenario.defaultNettingClientPrefMissing(context);
            } else {
                if (SettlementLevel.CLIENT == clientPreference.getSettlementLevel()) {
                    if (clientPreference.isNetting()) {
                        earningNettingScenario.nettingClientLevel(context);
                    } else {
                        earningNettingScenario.noNettingClientLevel(context);
                    }
                } else if (SettlementLevel.FUND == clientPreference.getSettlementLevel()) {
                    if (clientPreference.isNetting()) {
                        earningNettingScenario.nettingFundLevel(context);
                    } else {
                        earningNettingScenario.noNettingFundLevel(context);
                    }
                } else if (SettlementLevel.PAYMENT_FUND == clientPreference.getSettlementLevel()) {
                    if (clientPreference.isNetting()) {
                        earningNettingScenario.nettingPaymentFundLevel(context);
                    } else {
                        earningNettingScenario.noNettingPaymentFundLevel(context);
                    }
                }
            }

            SettlementUpload settlementUpload = context.getSettlementUpload();
        };
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public BaseHandler<EarningHandlerContext> fetchEarningPersistentHandler() {
        return context -> {
            try {
                List<NetEarning> netEarnings = context.getNetEarnings();
                long batchStartId = idGenerator.getNextIdBlock(netEarnings.size());
                String datePart = LocalDate.now().format(DATE_FORMATTER);

                for (int i = 0; i < netEarnings.size(); i++) {
                    NetEarning netEarning = netEarnings.get(i);
                    long currentId = batchStartId + i;
                    String id = idGenerator.formatId(datePart, currentId);
                    netEarning.setId(id);
                }
            } catch (UnexpectedRollbackException ex) {
                log.error(ex.getMostSpecificCause());
                log.info(ex.getRootCause());
            }
        };
    }
}
