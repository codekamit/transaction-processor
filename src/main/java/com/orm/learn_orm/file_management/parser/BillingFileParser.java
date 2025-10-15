package com.orm.learn_orm.file_management.parser;

import com.orm.learn_orm.dto.BillingDTO;
import com.orm.learn_orm.exception.ParsingException;
import com.orm.learn_orm.file_management.header.BillingFileHeader;
import com.orm.learn_orm.file_management.processor.CSVFileProcessor;
import com.orm.learn_orm.file_management.processor.ExcelFileProcessor;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orm.learn_orm.file_management.FileFieldSetter.processField;
import static com.orm.learn_orm.file_management.FileFieldSetter.processNumericField;


@Service
@AllArgsConstructor
public class BillingFileParser implements IFileParser<BillingDTO> {

    private final BillingFileHeader billingFileHeader;
    private final ExcelFileProcessor excelFileProcessor;
    private final CSVFileProcessor csvFileProcessor;

    public List<BillingDTO> parseFile(MultipartFile file) throws IOException {
        String extension = file.getOriginalFilename();
        List<Map<String, String>> errorDetails = new ArrayList<>();
        List<List<String>> billingData = null;
        List<BillingDTO> billingDTOs = new ArrayList<>();
        if (!StringUtils.isBlank(extension)) {
            if (extension.endsWith(".xlsx")) {
                billingData = excelFileProcessor.processFile(file, billingFileHeader);
            } else if (extension.endsWith(".csv")) {
                billingData = csvFileProcessor.processFile(file, billingFileHeader);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + extension);
            }
        }

        if (billingData == null) {
            throw new IllegalArgumentException("File is empty or could not be processed.");
        }

        billingFileHeader.allHeadersPresent(billingData.get(billingFileHeader.headerStartRow()), billingFileHeader);
        Map<String, Integer> headerMap = billingFileHeader.getHeaderIndices(billingData.get(billingFileHeader.headerStartRow()));
        int idx = billingFileHeader.headerStartRow() + 1;
        while (idx < billingData.size()) {
            BillingDTO billingDTO = new BillingDTO();
            List<String> row = billingData.get(idx++);
            if (billingFileHeader.hasReachedFooter(row, billingFileHeader))
                break;
            if (billingFileHeader.isRowEmpty(row))
                break;

            Map<String, String> errorMap = new HashMap<>();

            processField(row, headerMap, billingFileHeader.BROKER_SHORTNAME(), errorMap, billingDTO::setBrokerShortname);
            processField(row, headerMap, billingFileHeader.CURRENCY(), errorMap, billingDTO::setCurrency);
            processField(row, headerMap, billingFileHeader.BROKER_NUMBER(), errorMap, billingDTO::setBrokerNumber);
            processNumericField(row, headerMap, billingFileHeader.RECEIVED_AMOUNT(), errorMap, billingDTO::setReceivedAmount);

            billingDTO.isValid(errorMap);
            errorDetails.add(errorMap);
            billingDTOs.add(billingDTO);
            idx++;
        }

        if (!errorDetails.isEmpty()) {
            throw new ParsingException("Errors found during parsing.", errorDetails);
        }

        return billingDTOs;
    }
}
