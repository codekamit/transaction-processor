package com.orm.learn_orm.file_management.processor;


import com.orm.learn_orm.file_management.header.IFileHeader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component("excelFileProcessor")
public class ExcelFileProcessor implements IFileProcessor {

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return getStringCellValue(cell);
            case NUMERIC:
                return getNumericCellValue(cell);
            case BOOLEAN:
                return getBooleanCellValue(cell);
            case FORMULA:
                return getCellValueAsString(evaluateFormulaCell(cell));
            default:
                return null;
        }
    }

    private static String getStringCellValue(Cell cell) {
        String value = cell.getStringCellValue().trim();
        return StringUtils.isBlank(value) ? null : value;
    }

    private static String getNumericCellValue(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return getFormattedDateCellValue(cell);
        } else {
            return getPlainNumericCellValue(cell);
        }
    }

    private static String getFormattedDateCellValue(Cell cell) {
        String date = cell.getLocalDateTimeCellValue().toLocalDate()
                .format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        return StringUtils.isBlank(date) ? null : date;
    }

    private static String getPlainNumericCellValue(Cell cell) {
        double numericValue = cell.getNumericCellValue();
        return (numericValue == (long) numericValue) ? String.valueOf((long) numericValue) : String.valueOf(numericValue);
    }

    private static String getBooleanCellValue(Cell cell) {
        String value = String.valueOf(cell.getBooleanCellValue());
        return StringUtils.isBlank(value) ? null : value;
    }

    public static Cell evaluateFormulaCell(Cell cell) {
        if (cell == null) {
            return null;
        }

        Workbook workbook = cell.getSheet().getWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        try {
            return evaluator.evaluateInCell(cell);
        } catch (NotImplementedException ex) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue("");
            return cell;
        }
    }

    public List<List<String>> processFile(MultipartFile file, IFileHeader fileHeaders) throws IOException {
        List<List<String>> rawData = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(fileHeaders.sheetNumber());

            for (int i = fileHeaders.headerStartRow(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    break;
                }

                List<String> rowData = new ArrayList<>();
                for (int j = fileHeaders.headerStartCol(); j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.add(getCellValueAsString(cell));
                }
                rawData.add(rowData);
            }
        }
        return rawData;
    }
}










