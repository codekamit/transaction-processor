package com.orm.learn_orm.my_tests.workbook_test.generator;

import com.orm.learn_orm.my_tests.workbook_test.IExportable;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportNested;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportableAnnotationProcessor;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CsvGenerator {

    private static final int MAX_ROWS = 100_000;
    private static final String CHILD_INDENT_PREFIX = "  ";

    private final ExportableAnnotationProcessor annotationProcessor;

    public StreamingResponseBody createCsvStream(List<? extends IExportable> data) {

        if (data == null || data.isEmpty()) {
            return outputStream -> {
            }; // Return empty stream
        }

        try {
            Class<?> dtoClass = data.get(0).getClass();

            List<ExportableAnnotationProcessor.ProcessedColumn> parentMembers = annotationProcessor.getOrderedProcessedFields(dtoClass);
            Field nestedListField = annotationProcessor.getFieldByAnnotation(dtoClass, ExportNested.class);
            List<ExportableAnnotationProcessor.ProcessedColumn> childMembers = new ArrayList<>();

            if (nestedListField != null) {
                Class<?> childDtoClass = annotationProcessor.getGenericTypeOfList(nestedListField);
                if (childDtoClass != null && IExportable.class.isAssignableFrom(childDtoClass)) {
                    nestedListField.setAccessible(true);
                    childMembers = annotationProcessor.getOrderedProcessedFields(childDtoClass);
                } else {
                    nestedListField = null;
                }
            }

            // --- Final-by-effectively-final copies for use in lambda ---
            final List<ExportableAnnotationProcessor.ProcessedColumn> finalParentMembers = parentMembers;
            final Field finalNestedListField = nestedListField;
            final List<ExportableAnnotationProcessor.ProcessedColumn> finalChildMembers = childMembers;

            // Build the master header list
            List<String> masterHeaderList = new ArrayList<>();
            Set<String> headersAdded = new HashSet<>();
            finalParentMembers.forEach(pc -> {
                if (headersAdded.add(pc.headerName)) masterHeaderList.add(pc.headerName);
            });
            finalChildMembers.forEach(pc -> {
                if (headersAdded.add(pc.headerName)) masterHeaderList.add(pc.headerName);
            });

            // Build maps for quick lookup
            Map<String, ExportableAnnotationProcessor.ProcessedColumn> parentFieldMap = finalParentMembers.stream()
                    .collect(Collectors.toMap(pc -> pc.headerName, pc -> pc, (o1, o2) -> o1));
            Map<String, ExportableAnnotationProcessor.ProcessedColumn> childFieldMap = finalChildMembers.stream()
                    .collect(Collectors.toMap(pc -> pc.headerName, pc -> pc, (o1, o2) -> o1));

            return outputStream -> {
                int rowNum = 0;
                try (
                        Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                                .withHeader(masterHeaderList.toArray(new String[0])))
                ) {

                    for (Object parentDto : data) {
                        if (rowNum++ >= MAX_ROWS) break;

                        // --- Print Parent Row ---
                        List<Object> parentRecord = new ArrayList<>();
                        for (String header : masterHeaderList) {
                            ExportableAnnotationProcessor.ProcessedColumn pc = parentFieldMap.get(header);
                            Object value = (pc != null) ? pc.getValue(parentDto) : null;
                            parentRecord.add(value);
                        }
                        csvPrinter.printRecord(parentRecord);

                        // --- Print Child Rows (if any) ---
                        if (finalNestedListField != null && !finalChildMembers.isEmpty()) {
                            List<?> children = (List<?>) finalNestedListField.get(parentDto);
                            if (children != null && !children.isEmpty()) {
                                for (Object childDto : children) {
                                    if (rowNum++ >= MAX_ROWS) break;

                                    List<Object> childRecord = new ArrayList<>();
                                    for (int colNum = 0; colNum < masterHeaderList.size(); colNum++) {
                                        String header = masterHeaderList.get(colNum);
                                        ExportableAnnotationProcessor.ProcessedColumn pc = childFieldMap.get(header);
                                        Object value = (pc != null) ? pc.getValue(childDto) : null;

                                        if (colNum == 0 && value != null) {
                                            childRecord.add(CHILD_INDENT_PREFIX + value);
                                        } else {
                                            childRecord.add(value);
                                        }
                                    }
                                    csvPrinter.printRecord(childRecord);
                                }
                            }
                        }
                    } // end parent loop
                    csvPrinter.flush();
                } catch (Exception e) {
                    System.err.println("Error writing CSV stream: " + e.getMessage());
                    throw new RuntimeException("Error writing CSV stream", e);
                }
            };

        } catch (Exception e) {
            throw new RuntimeException("Error preparing CSV export: " + e.getMessage(), e);
        }
    }
}