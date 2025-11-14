package com.orm.learn_orm.settlement_processor.preprocessor;

import com.orm.learn_orm.config.DailySequenceGenerator;
import com.orm.learn_orm.dto.ClientPrefKey;
import com.orm.learn_orm.dto.EarningDTO;
import com.orm.learn_orm.dto.EarningProcessDTO;
import com.orm.learn_orm.enums.SettlementType;
import com.orm.learn_orm.enums.UploadStatus;
import com.orm.learn_orm.factory.AgencyFactory;
import com.orm.learn_orm.file_management.parser.EarningFileParser;
import com.orm.learn_orm.file_management.parser.IFileParser;
import com.orm.learn_orm.mapper.ISettlementMapper;
import com.orm.learn_orm.model.ClientPreference;
import com.orm.learn_orm.model.Earning;
import com.orm.learn_orm.model.NetEarning;
import com.orm.learn_orm.model.SettlementUpload;
import com.orm.learn_orm.repo.IClientPreferenceRepo;
import com.orm.learn_orm.service.SettlementUploadService;
import com.orm.learn_orm.settlement_processor.processor.EarningProcessor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Log4j2
@Component("earningPreprocessorV2")
@RequiredArgsConstructor
public class EarningSettlementPreprocessorV2 implements ISettlementPreprocessor {

    private static final ISettlementMapper SETTLEMENT_MAPPER = ISettlementMapper.INSTANCE;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    private final DailySequenceGenerator idGenerator;
    private final SettlementUploadService settlementUploadService;
    private final IClientPreferenceRepo clientPreferenceRepo;

    @PersistenceContext(name = "ormEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    @Transactional(transactionManager = "ormTransactionManager", rollbackFor = Exception.class)
    public void preprocess(MultipartFile file, AgencyFactory<?,?,?,?> agencyFactory) throws IOException {

        SettlementUpload settlementUpload = new SettlementUpload();
        updateSettlementUpload(settlementUpload, file);

        try {
            IFileParser<EarningDTO> fileParser = (EarningFileParser) agencyFactory.getParser();
            EarningProcessDTO earningProcessDTO = (EarningProcessDTO) agencyFactory.getSettlementProcessDTO();
            List<EarningDTO> earningDTOs = fileParser.parseFile(file);

            if (earningDTOs == null || earningDTOs.isEmpty()) {
                log.warn("Earning file is empty: " + file.getOriginalFilename());
                settlementUpload.setUploadStatus(UploadStatus.SUCCESS);
                settlementUploadService.persist(settlementUpload);
                return;
            }

            settlementUpload.setUploadStatus(UploadStatus.SUCCESS);

            long batchStartId = idGenerator.getNextIdBlock(earningDTOs.size());
            String datePart = LocalDate.now().format(DATE_FORMATTER);
            List<Earning> persistedEarnings = earningDTOs.stream()
                    .map(SETTLEMENT_MAPPER::getEarning)
                    .toList();

            List<ClientPrefKey> keys = new ArrayList<>();
            persistedEarnings.forEach(earning -> {
                keys.add(new ClientPrefKey(earning.getClientName(), earning.getCurrency()));
            });
            List<ClientPreference> clientPreferences = clientPreferenceRepo.findPrefWithFundMappingForClientKeys(ClientPreference.getFormattedKeys(keys));


            for (int i = 0; i < persistedEarnings.size(); i++) {
                Earning earning = persistedEarnings.get(i);
                long currentId = batchStartId + i;
                String id = idGenerator.formatId(datePart, currentId);
                earning.setId(id);
                earning.setSettlementUpload(settlementUpload);
            }

            earningProcessDTO.setSettlementUpload(settlementUpload);
            earningProcessDTO.setEarnings(persistedEarnings);
            earningProcessDTO.setClientPreferences(clientPreferences);
            EarningProcessor earningProcessor = (EarningProcessor) agencyFactory.getProcessor();
            List<NetEarning> netEarnings = earningProcessor.processSettlement(earningProcessDTO);
            entityManager.persist(settlementUpload);
            netEarnings.forEach(netEarning -> {
                entityManager.persist(netEarning);
            });
            persistedEarnings.forEach(earning -> {
                entityManager.persist(earning);
            });
        } catch (IOException ex) {
            log.error("Failed to process settlement file, transaction will be rolled back.", ex);
            throw ex;
        } catch(Exception ex) {
            log.error("Failed to process settlement file, transaction will be rolled back.", ex);
            settlementUpload.setUploadStatus(UploadStatus.FAILURE);
        }
    }

    public void updateSettlementUpload(SettlementUpload settlementUpload, MultipartFile file) {
        settlementUpload.setFileName(file.getOriginalFilename());
        settlementUpload.setFileSize(file.getSize() + "KB");
        settlementUpload.setSettlementType(SettlementType.EARNING);
    }

    public void updateSettlementUpload(SettlementUpload settlementUpload, String fileName, String fileSize) {
        settlementUpload.setFileName(fileName);
        settlementUpload.setFileSize(fileSize);
        settlementUpload.setSettlementType(SettlementType.EARNING);
    }
}
