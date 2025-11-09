package com.orm.learn_orm.my_tests.workbook_test;


import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDate;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class ExcelProcessing {

    private ExcelService excelService;

    @GetMapping("excel")
    public ResponseEntity<StreamingResponseBody> downloadOrderReport_Workbook() {
        String filename = "AuthorReport-" + LocalDate.now() + ".xlsx";
        Workbook workbook = excelService.getExcelWorkbook();
        StreamingResponseBody stream = IExportable.generateWorkBookStream(workbook);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(stream);
    }

    @GetMapping("csv")
    public ResponseEntity<StreamingResponseBody> downloadOrderReport_CSV() {
        String filename = "AuthorReport-" + LocalDate.now() + ".csv";
        StreamingResponseBody stream = excelService.getCSV();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(stream);
    }
}
