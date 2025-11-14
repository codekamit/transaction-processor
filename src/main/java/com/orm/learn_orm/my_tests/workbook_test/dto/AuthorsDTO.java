package com.orm.learn_orm.my_tests.workbook_test.dto;

import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportColumn;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportNested;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PartialExport;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PerfectExport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AuthorsDTO implements IExportable {
    @ExportColumn(name = "Name", order = 1)
    private String name;
    @ExportColumn(name = "Age", order = 2)
    private int age;
    @ExportColumn(name = "Born In", order = 3)
    private String bornIn;
    @ExportNested
    private List<BooksDTO> books;
    @ExportColumn(name = "Internal Notes", order = 4, groups = {PartialExport.class})
    private String internalNotes;
    @ExportColumn(name = "Test Runs", order = 5, groups = {PartialExport.class})
    private int testRuns;
    @ExportColumn(name = "One Day Runs", order = 6, groups = {PerfectExport.class})
    private int oneDayRuns;
    @ExportColumn(name = "First Class Runs", order = 7, groups = {PerfectExport.class})
    private int firstClassRuns;
    @ExportColumn(name="Total Runs", order = 8)
    private int totalRuns() {
        return this.oneDayRuns + this.testRuns + this.firstClassRuns;
    }
}
