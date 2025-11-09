package com.orm.learn_orm.my_tests.workbook_test.generator;

import com.orm.learn_orm.my_tests.workbook_test.IExportable;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportNested;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportableAnnotationProcessor;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class WorkBookGenerator {

    private static final int MAX_ROWS = 100_000;
    private static final short INDENTATION_SIZE = 1;
    private static final int SXSSF_WINDOW_SIZE = 100;

    private final ExportableAnnotationProcessor annotationProcessor;
    
    public Workbook createWorkbook(List<? extends IExportable> data, String sheetName) {

        Workbook workbook = new SXSSFWorkbook(SXSSF_WINDOW_SIZE);
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(sheetName);
        sheet.trackAllColumnsForAutoSizing();

        if (data == null || data.isEmpty()) {
            return workbook;
        }

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dateCellStyle = createDateCellStyle(workbook, "yyyy-mm-dd");
        CellStyle dateTimeCellStyle = createDateCellStyle(workbook, "yyyy-mm-dd hh:mm:ss");
        CellStyle decimalCellStyle = createDecimalCellStyle(workbook);

        CellStyle indentedStyle = workbook.createCellStyle();
        indentedStyle.setIndention(INDENTATION_SIZE);

        CellStyle indentedDateStyle = workbook.createCellStyle();
        indentedDateStyle.cloneStyleFrom(dateCellStyle);
        indentedDateStyle.setIndention(INDENTATION_SIZE);

        CellStyle indentedDateTimeStyle = workbook.createCellStyle();
        indentedDateTimeStyle.cloneStyleFrom(dateTimeCellStyle);
        indentedDateTimeStyle.setIndention(INDENTATION_SIZE);

        CellStyle indentedDecimalStyle = workbook.createCellStyle();
        indentedDecimalStyle.cloneStyleFrom(decimalCellStyle);
        indentedDecimalStyle.setIndention(INDENTATION_SIZE);

        try {
            Class<?> dtoClass = data.get(0).getClass();

            List<ExportableAnnotationProcessor.ProcessedColumn> parentMembers = annotationProcessor.getOrderedProcessedFields(dtoClass);
            Field nestedListField = annotationProcessor.getFieldByAnnotation(dtoClass, ExportNested.class);
            List<ExportableAnnotationProcessor.ProcessedColumn> childMembers = new ArrayList<>();

            if (nestedListField != null) {
                if (!List.class.isAssignableFrom(nestedListField.getType())) {
                    throw new RuntimeException(String.format(
                            "Invalid @ExcelNested usage: Field '%s' in class '%s' is not a List. Found type: %s",
                            nestedListField.getName(), dtoClass.getSimpleName(), nestedListField.getType().getSimpleName()
                    ));
                }
                Class<?> childDtoClass = annotationProcessor.getGenericTypeOfList(nestedListField);
                if (childDtoClass == null) {
                    throw new RuntimeException(String.format(
                            "Invalid @ExcelNested usage: Field '%s' in class '%s' must be a generic List (e.g., List<MyDTO>). Found raw List.",
                            nestedListField.getName(), dtoClass.getSimpleName()
                    ));
                }
                if (!IExportable.class.isAssignableFrom(childDtoClass)) {
                    throw new RuntimeException(String.format(
                            "Invalid @ExcelNested usage: The type '%s' in field '%s' does not implement 'ExcelExportable'.",
                            childDtoClass.getSimpleName(), nestedListField.getName()
                    ));
                }
                nestedListField.setAccessible(true);
                childMembers = annotationProcessor.getOrderedProcessedFields(childDtoClass);
            }

            List<String> masterHeaderList = new ArrayList<>();
            Set<String> headersAdded = new HashSet<>();

            for (ExportableAnnotationProcessor.ProcessedColumn pc : parentMembers) {
                if (headersAdded.add(pc.headerName)) {
                    masterHeaderList.add(pc.headerName);
                }
            }
            for (ExportableAnnotationProcessor.ProcessedColumn pc : childMembers) {
                if (headersAdded.add(pc.headerName)) {
                    masterHeaderList.add(pc.headerName);
                }
            }

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < masterHeaderList.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(masterHeaderList.get(i));
                cell.setCellStyle(headerStyle);
            }

            Map<String, ExportableAnnotationProcessor.ProcessedColumn> parentFieldMap = parentMembers.stream()
                    .collect(Collectors.toMap(pc -> pc.headerName, pc -> pc, (o1, o2) -> o1));
            Map<String, ExportableAnnotationProcessor.ProcessedColumn> childFieldMap = childMembers.stream()
                    .collect(Collectors.toMap(pc -> pc.headerName, pc -> pc, (o1, o2) -> o1));

            int rowNum = 1;
            boolean limitReached = false;
            for (Object parentDto : data) {
                if (rowNum >= MAX_ROWS) { limitReached = true; break; }

                Row parentRow = sheet.createRow(rowNum++);
                int parentStartRowForGrouping = rowNum;

                for (int colNum = 0; colNum < masterHeaderList.size(); colNum++) {
                    String header = masterHeaderList.get(colNum);
                    ExportableAnnotationProcessor.ProcessedColumn pc = parentFieldMap.get(header);
                    Object value = (pc != null) ? pc.getValue(parentDto) : null;
                    createCell(parentRow, colNum, value, null,
                            dateCellStyle, dateTimeCellStyle, decimalCellStyle);
                }

                if (nestedListField != null && !childMembers.isEmpty()) {
                    List<?> children = (List<?>) nestedListField.get(parentDto);
                    if (children != null && !children.isEmpty()) {
                        for (Object childDto : children) {
                            if (rowNum >= MAX_ROWS) { limitReached = true; break; }
                            Row childRow = sheet.createRow(rowNum++);
                            for (int colNum = 0; colNum < masterHeaderList.size(); colNum++) {
                                String header = masterHeaderList.get(colNum);
                                ExportableAnnotationProcessor.ProcessedColumn pc = childFieldMap.get(header);
                                Object value = (pc != null) ? pc.getValue(childDto) : null;
                                createCell(childRow, colNum, value, indentedStyle,
                                        indentedDateStyle, indentedDateTimeStyle, indentedDecimalStyle);
                            }
                        }
                        int parentEndRowForGrouping = rowNum - 1;
                        if (parentEndRowForGrouping >= parentStartRowForGrouping) {
                            sheet.groupRow(parentStartRowForGrouping, parentEndRowForGrouping);
                            sheet.setRowGroupCollapsed(parentStartRowForGrouping, true);
                        }
                    }
                }
                if (limitReached) break;
            }

            for (int i = 0; i < masterHeaderList.size(); i++) {
                sheet.autoSizeColumn(i);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel: " + e.getMessage(), e);
        }
        return workbook;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle createDateCellStyle(Workbook workbook, String format) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(format));
        return style;
    }

    private CellStyle createDecimalCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private void createCell(Row row, int column, Object value,
                                   CellStyle baseStyle,
                                   CellStyle dateCellStyle,
                                   CellStyle dateTimeCellStyle,
                                   CellStyle decimalCellStyle) {

        Cell cell = row.createCell(column);
        if (baseStyle != null) {
            cell.setCellStyle(baseStyle);
        }

        if (value == null) {
            cell.setBlank();
            return;
        }

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
            cell.setCellStyle(dateCellStyle);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
            cell.setCellStyle(dateTimeCellStyle);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateTimeCellStyle);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
            cell.setCellStyle(decimalCellStyle);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}

