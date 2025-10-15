package com.orm.learn_orm.settlement_processor.handler_lambda;

import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.enums.State;
import com.orm.learn_orm.mapper.ISettlementMapper;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.settlement_processor.handlers.EarningHandlerContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EarningNettingScenario {

    private static final ISettlementMapper SETTLEMENT_MAPPER = ISettlementMapper.INSTANCE;

    @Transactional(transactionManager = "ormTransactionManager")
    public void noNettingClientLevel(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        ClientPrefProcessingDTO clientPreference = earningHandlerContext.getClientPreference();
        if(clientPreference.getFundMapping() == null || clientPreference.getFundMapping().isEmpty()) {
            earningHandlerContext.getEarnings().forEach(earning -> {
                NetEarning netEarning = defaultNettingFundMapMissing(earning, settlementUpload);
                earningHandlerContext.addNetEarning(netEarning);
            });
            return;
        }
        earningHandlerContext.getEarnings()
                .forEach(earning -> {
                    earning.setState(State.PROCESSED);
                    NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningFromRefEarning(earning, clientPreference);
                    netEarning.setAmount(earning.getAmount());
                    netEarning.setPaymentFund(clientPreference.getPaymentFund());
                    netEarning.setState(State.PROCESSED);
                    netEarning.addEarning(earning);
                    settlementUpload.addNetEarning(netEarning);
                    earningHandlerContext.addNetEarning(netEarning);
                });
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void noNettingFundLevel(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        ClientPrefProcessingDTO clientPreference = earningHandlerContext.getClientPreference();
        earningHandlerContext.getEarnings()
                .forEach(earning -> {
                    earning.setState(State.PROCESSED);
                    NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningFromRefEarning(earning, clientPreference);
                    netEarning.setAmount(earning.getAmount());
                    netEarning.setPaymentFund(earning.getFund());
                    netEarning.setState(State.PROCESSED);
                    netEarning.addEarning(earning);
                    settlementUpload.addNetEarning(netEarning);
                    earningHandlerContext.addNetEarning(netEarning);
                });
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void noNettingPaymentFundLevel(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        ClientPrefProcessingDTO clientPreference = earningHandlerContext.getClientPreference();
        if(clientPreference.getFundMapping() == null || clientPreference.getFundMapping().isEmpty()) {
            earningHandlerContext.getEarnings().forEach(earning -> {
                NetEarning netEarning = defaultNettingFundMapMissing(earning, settlementUpload);
                earningHandlerContext.addNetEarning(netEarning);
            });
            return;
        }
        Map<String, String> fundMap = clientPreference.getFundMapping();
        earningHandlerContext.getEarnings()
                .forEach(earning -> {
                    if(!fundMap.containsKey(earning.getFund())) {
                        NetEarning netEarning = defaultNettingFundMapMissing(earning, settlementUpload);
                        earningHandlerContext.addNetEarning(netEarning);
                    }
                    else {
                        earning.setState(State.PROCESSED);
                        NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningFromRefEarning(earning, clientPreference);
                        netEarning.setAmount(earning.getAmount());
                        netEarning.setPaymentFund(fundMap.get(earning.getFund()));
                        netEarning.setState(State.PROCESSED);
                        netEarning.addEarning(earning);
                        settlementUpload.addNetEarning(netEarning);
                        earningHandlerContext.addNetEarning(netEarning);
                    }
                });
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void nettingClientLevel(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        ClientPrefProcessingDTO clientPreference = earningHandlerContext.getClientPreference();
        if(clientPreference.getFundMapping() == null || clientPreference.getFundMapping().isEmpty()) {
            earningHandlerContext.getEarnings().forEach(earning -> {
                NetEarning netEarning = defaultNettingFundMapMissing(earning, settlementUpload);
                earningHandlerContext.addNetEarning(netEarning);
            });
            return;
        }
        Earning refEarning = earningHandlerContext.getEarnings().get(0);
        NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningFromRefEarning(refEarning, clientPreference);
        netEarning.setState(State.PROCESSED);
        netEarning.setAmount(earningHandlerContext.getEarnings()
                .stream()
                .mapToDouble(Earning::getAmount)
                .sum());

        earningHandlerContext.getEarnings()
                .forEach(earning -> {
                    netEarning.addEarning(earning);
                    earning.setState(State.PROCESSED);
                });
        netEarning.setPaymentFund(clientPreference.getPaymentFund());
        settlementUpload.addNetEarning(netEarning);
        earningHandlerContext.addNetEarning(netEarning);
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void nettingFundLevel(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        Map<String, List<Earning>> fundEarningsMap = earningHandlerContext.getEarnings().stream()
                .collect(Collectors.groupingBy(Earning::getFund));
        ClientPrefProcessingDTO clientPreference = earningHandlerContext.getClientPreference();
        fundEarningsMap.forEach((fund, earnings) -> {
            Earning refEarning = earnings.get(0);
            NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningFromRefEarning(refEarning, clientPreference);
            netEarning.setState(State.PROCESSED);
            netEarning.setAmount(earnings
                    .stream()
                    .mapToDouble(Earning::getAmount)
                    .sum());

            netEarning.setPaymentFund(fund);
            earnings.forEach(earning -> {
                netEarning.addEarning(earning);
                earning.setState(State.PROCESSED);
            });
            settlementUpload.addNetEarning(netEarning);
            earningHandlerContext.addNetEarning(netEarning);
        });
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void nettingPaymentFundLevel(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        ClientPrefProcessingDTO clientPreference = earningHandlerContext.getClientPreference();
        if(clientPreference.getFundMapping() == null || clientPreference.getFundMapping().isEmpty()) {
            earningHandlerContext.getEarnings().forEach(earning -> {
                NetEarning netEarning = defaultNettingFundMapMissing(earning, settlementUpload);
                earningHandlerContext.addNetEarning(netEarning);
            });
            return;
        }

        Map<String, String> fundGroupMap = clientPreference.getFundMapping();
        Map<String, List<Earning>> paymentFundEarningsMap = earningHandlerContext.getEarnings().stream()
                        .collect(Collectors.groupingBy(earning -> fundGroupMap.get(earning.getFund())));

        paymentFundEarningsMap.forEach((paymentFund, earnings) -> {
            if(paymentFund == null) {
                earnings.forEach(earning -> {
                    NetEarning netEarning = defaultNettingFundMapMissing(earning, settlementUpload);
                    earningHandlerContext.addNetEarning(netEarning);
                });
            }
            else {
                Earning refEarning = earnings.get(0);
                NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningFromRefEarning(refEarning, clientPreference);
                netEarning.setState(State.PROCESSED);
                netEarning.setAmount(earnings
                        .stream()
                        .mapToDouble(Earning::getAmount)
                        .sum());
                netEarning.setPaymentFund(paymentFund);
                earnings.forEach(earning -> {
                    netEarning.addEarning(earning);
                    earning.setState(State.PROCESSED);
                });
                settlementUpload.addNetEarning(netEarning);
                earningHandlerContext.addNetEarning(netEarning);
            }
        });
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public void defaultNettingClientPrefMissing(EarningHandlerContext earningHandlerContext) {
        SettlementUpload settlementUpload = earningHandlerContext.getSettlementUpload();
        earningHandlerContext.getEarnings()
                .forEach(earning -> {
                    earning.setState(State.PROCESSED_WITH_DEFAULT_PREF_MISSING);
                    NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningForMissingPref(earning);
                    netEarning.addEarning(earning);
                    settlementUpload.addNetEarning(netEarning);
                    earningHandlerContext.addNetEarning(netEarning);
                });
    }

    @Transactional(transactionManager = "ormTransactionManager")
    public NetEarning defaultNettingFundMapMissing(Earning earning, SettlementUpload settlementUpload) {
        earning.setState(State.PROCESSED_WITH_DEFAULT_FUND_GROUP_MISSING);
        NetEarning netEarning = SETTLEMENT_MAPPER.getNetEarningForMissingFundMap(earning);
        netEarning.addEarning(earning);
        settlementUpload.addNetEarning(netEarning);
        return netEarning;
    }
}
