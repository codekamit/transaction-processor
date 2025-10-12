package com.orm.learn_orm.repo;

import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.SettlementUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEarningRepo extends JpaRepository<Earning, Long> {

    @Query("SELECT DISTINCT e FROM Earning e JOIN FETCH e.settlementUpload WHERE e.settlementUpload = :settlementUpload")
    List<Earning> findAllEarningsWithSettlementUpload(SettlementUpload settlementUpload);
}
