package com.orm.learn_orm.my_tests.workbook_test.dto;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

public interface IExportable {

    static StreamingResponseBody generateWorkBookStream(Workbook workbook) {
        StreamingResponseBody stream = outputStream -> {
            try {
                workbook.write(outputStream);
            } catch (IOException e) {
                System.err.println("Error writing workbook to output stream: " + e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    System.err.println("Error closing workbook: " + e.getMessage());
                }
            }
        };
        return stream;
    }
}

