package com.orm.learn_orm.settlement_processor.handlers;

import com.orm.learn_orm.dto.ClientPrefProcessingDTO;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import com.orm.learn_orm.model.SettlementUpload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EarningHandlerContext extends HandlerContext {
    private ClientPrefProcessingDTO clientPreference;
    private List<Earning> earnings;
    private List<NetEarning> netEarnings;
    private SettlementUpload settlementUpload;

    public void addEarnings(Earning earning) {
        this.earnings.add(earning);
    }

    public void addNetEarning(NetEarning netEarning) {
        this.netEarnings.add(netEarning);
    }

    public void addEarnings(List<Earning> earnings) {
        this.earnings.addAll(earnings);
    }

    public void addNetEarnings(List<NetEarning> netEarnings) {
        this.netEarnings.addAll(netEarnings);
    }

}
