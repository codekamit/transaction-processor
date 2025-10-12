package com.orm.learn_orm.repo;

import com.orm.learn_orm.model.SettlementUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISettlementUploadRepo extends JpaRepository<SettlementUpload, Long> {
}
