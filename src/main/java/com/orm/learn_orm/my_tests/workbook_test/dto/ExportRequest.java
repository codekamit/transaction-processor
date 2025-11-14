package com.orm.learn_orm.my_tests.workbook_test.dto;

import com.orm.learn_orm.my_tests.workbook_test.enums.ExportFor;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExportRequest<T extends IExportable> {
    private List<T> exportableData;
    private String sheetName;
    private String fileName;
    private ExportFor exportFor;
}
