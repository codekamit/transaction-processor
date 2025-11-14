package com.orm.learn_orm.my_tests.workbook_test.service;


import com.orm.learn_orm.my_tests.workbook_test.dto.ExportRequest;
import com.orm.learn_orm.my_tests.workbook_test.dto.IExportable;
import com.orm.learn_orm.my_tests.workbook_test.generator.CsvGenerator;
import com.orm.learn_orm.my_tests.workbook_test.generator.WorkBookGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Log4j2
@Service
@AllArgsConstructor
public class PaymentExporterService {

    private static final String WORKBOOK_GENERATION_FAILED_LOG = "WorkBook generation failed.";
    private static final String WORKBOOK_GENERATION_SUCCESSFUL_LOG = "WorkBook generation successful.";
    private static final String CSV_GENERATION_FAILED_LOG = "WorkBook generation failed.";
    private static final String CSV_GENERATION_SUCCESSFUL_LOG = "CSV generation successful.";

    private final WorkBookGenerator workBookGenerator;
    private final CsvGenerator csvGenerator;

    public StreamingResponseBody generateWorkBook(ExportRequest<?> exportRequest) {
        Workbook workbook = null;
        try {
            workbook = workBookGenerator.createWorkbook(exportRequest);
            log.info(WORKBOOK_GENERATION_SUCCESSFUL_LOG);
            return IExportable.generateWorkBookStream(workbook);
        } catch(Exception ex) {
            log.error(WORKBOOK_GENERATION_FAILED_LOG, ex.getCause());
            throw new RuntimeException(WORKBOOK_GENERATION_FAILED_LOG);
        }
    }

    public StreamingResponseBody generateCSV(ExportRequest<?> exportRequest) {
        try {
            StreamingResponseBody stream = csvGenerator.createCsvStream(exportRequest);
            log.info(CSV_GENERATION_SUCCESSFUL_LOG);
            return stream;
        } catch(Exception ex) {
            log.error(CSV_GENERATION_FAILED_LOG, ex.getCause());
            throw new RuntimeException(CSV_GENERATION_FAILED_LOG);
        }
    }
}

