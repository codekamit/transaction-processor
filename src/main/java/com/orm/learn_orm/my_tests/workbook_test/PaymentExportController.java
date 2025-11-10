package com.orm.learn_orm.my_tests.workbook_test;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;


@Log4j2
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class PaymentExportController {

    private static final String WORKBOOK_GENERATION_FAILED_LOG = "WorkBook generation failed.";
    private static final String WORKBOOK_GENERATION_SUCCESS_LOG = "WorkBook generation completed successfully.";

    private final PaymentExporterService paymentExporterService;
    private final ExcelService excelService;

    @PostMapping("/workbook")
    public <T extends IExportable> ResponseEntity<StreamingResponseBody> exportDataToExcel(@RequestBody GenericExportRequest exportRequest) {
        try {
            StreamingResponseBody stream = paymentExporterService.generateWorkBook(exportRequest);
            String fileName = exportRequest.getFileName() + LocalDateTime.now() + ".xlsx";
            log.info(WORKBOOK_GENERATION_SUCCESS_LOG);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(stream);
        } catch (Exception ex) {
            log.error(WORKBOOK_GENERATION_FAILED_LOG, ex);
            throw ex;
        }
    }

    @GetMapping("/csv")
    public <T extends IExportable> ResponseEntity<StreamingResponseBody> exportDataToCsv(@RequestBody GenericExportRequest exportRequest) {
        try {
            StreamingResponseBody stream = paymentExporterService.generateCSV(exportRequest);
            String fileName = exportRequest.getFileName() + LocalDateTime.now() + ".csv";
            log.info(WORKBOOK_GENERATION_SUCCESS_LOG);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(stream);
        } catch (Exception ex) {
            log.error(WORKBOOK_GENERATION_FAILED_LOG, ex);
            throw ex;
        }
    }

    @GetMapping("/hit")
    public void callAPI() {
        excelService.callApi();
    }
}
