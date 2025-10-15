package com.orm.learn_orm.file_management.parser;

import com.orm.learn_orm.dto.EarningDTO;
import com.orm.learn_orm.exception.ParsingException;
import com.orm.learn_orm.file_management.header.EarningFileHeader;
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
public class EarningFileParser implements IFileParser<EarningDTO> {

    private final EarningFileHeader earningFileHeaders;
    private final ExcelFileProcessor excelFileProcessor;
    private final CSVFileProcessor csvFileProcessor;

    public List<EarningDTO> parseFile(MultipartFile file) throws IOException {
        String extension = file.getOriginalFilename();
        List<List<String>> earningData = null;
        List<EarningDTO> earningDTOs = new ArrayList<>();
        List<Map<String, String>> errorDetails = new ArrayList<>();
        if (!StringUtils.isBlank(extension)) {
            if (extension.endsWith(".xlsx")) {
                earningData = excelFileProcessor.processFile(file, earningFileHeaders);
            } else if (extension.endsWith(".csv")) {
                earningData = csvFileProcessor.processFile(file, earningFileHeaders);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + extension);
            }
        }

        if (earningData == null) {
            throw new IllegalArgumentException("File is empty or could not be processed.");
        }

        earningFileHeaders.allHeadersPresent(earningData.get(earningFileHeaders.headerStartRow()), earningFileHeaders);
        Map<String, Integer> headerMap = earningFileHeaders.getHeaderIndices(earningData.get(0));
        int idx = earningFileHeaders.headerStartRow() + 1;
        while (idx < earningData.size()) {
            EarningDTO earningDTO = new EarningDTO();
            List<String> row = earningData.get(idx++);
            if (earningFileHeaders.hasReachedFooter(row, earningFileHeaders))
                break;
            if (earningFileHeaders.isRowEmpty(row))
                break;

            Map<String, String> errorMap = new HashMap<>();

            processField(row, headerMap, earningFileHeaders.CLIENT_NAME(), errorMap, earningDTO::setClientName);
            processField(row, headerMap, earningFileHeaders.CURRENCY(), errorMap, earningDTO::setCurrency);
            processField(row, headerMap, earningFileHeaders.FUND(), errorMap, earningDTO::setFund);
            processNumericField(row, headerMap, earningFileHeaders.AMOUNT(), errorMap, earningDTO::setAmount);

            if (earningDTO.isValid(errorMap)) {
                earningDTOs.add(earningDTO);
            }

            if (!errorMap.isEmpty()) {
                errorDetails.add(errorMap);
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new ParsingException("Errors found during parsing.", errorDetails);
        }
        return earningDTOs;
    }
}
