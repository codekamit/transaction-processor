package com.orm.learn_orm.my_tests.workbook_test;


import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * This is the new, NON-GENERIC DTO for your controller.
 * It uses a "reportType" string to identify the DTO.
 * It holds the data as a "raw" List<Object>.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GenericExportRequest {
    private List<Object> exportableData;
    private String sheetName;
    private String fileName;
    private String activeGroup;
}
