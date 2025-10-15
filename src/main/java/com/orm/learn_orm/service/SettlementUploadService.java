package com.orm.learn_orm.service;

import com.orm.learn_orm.repo.ISettlementUploadRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettlementUploadService {

    private final ISettlementUploadRepo settlementUploadRepo;

}
