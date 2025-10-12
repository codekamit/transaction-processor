package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.dto.EarningDTO;
import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.UploadStatus;
import com.orm.learn_orm.file_management.parser.EarningFileParser;
import com.orm.learn_orm.mapper.ISettlementMapper;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.IEarningRepo;
import com.orm.learn_orm.repo.ISettlementUploadRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@Component("earningPreprocessor")
@AllArgsConstructor
public class EarningPreprocessor implements IPreprocessor {

    private static final ISettlementMapper SETTLEMENT_MAPPER = ISettlementMapper.INSTANCE;

    private final EarningFileParser parser;
    private final ISettlementUploadRepo settlementUploadRepo;
    private final IEarningRepo earningRepo;

    @Transactional(transactionManager = "ormTransactionManager")
    @Override
    public void preprocess(MultipartFile file) throws IOException {
        SettlementUpload settlementUpload = new SettlementUpload();
        try {
            List<EarningDTO> earningDTOs = parser.parseFile(file);
            List<Earning> earnings = earningDTOs.stream()
                    .map(SETTLEMENT_MAPPER::getEarning)
                    .peek(earning -> earning.setSettlementUpload(settlementUpload))
                    .toList();

            settlementUpload.setEarnings(earnings);
            updateSettlementUpload(settlementUpload, file);
            settlementUpload.setUploadStatus(UploadStatus.SUCCESS);
            SettlementUpload persistedUpload = settlementUploadRepo.save(settlementUpload);
            List<Earning> persistedEarnings = earningRepo.findAllEarningsWithSettlementUpload(persistedUpload);

        } catch (Exception ex) {
            updateSettlementUpload(settlementUpload, file);
            settlementUpload.setUploadStatus(UploadStatus.FAILURE);
            settlementUploadRepo.save(settlementUpload);
        }
    }

    public void updateSettlementUpload(SettlementUpload settlementUpload, MultipartFile file) {
        settlementUpload.setFileName(file.getOriginalFilename());
        settlementUpload.setFileSize((file.getSize() / 1024) + "KB");
        settlementUpload.setSettlementType(SettlementType.EARNING);
    }
}
