package com.orm.learn_orm.my_tests.workbook_test;

import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportColumn;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportNested;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorsDTO implements IExportable {
    @ExportColumn(name = "Name", order = 1)
    private String name;
    @ExportColumn(name = "Age", order = 2)
    private int age;
    @ExportColumn(name = "Born In", order = 3)
    private String bornIn;
    @ExportNested
    private List<BooksDTO> books;
    private String internalNotes;
    private int testRuns;
    private int oneDayRuns;
    private int firstClassRuns;
    @ExportColumn(name="Total Runs", order = 4)
    private int totalRuns() {
        return this.oneDayRuns + this.testRuns + this.firstClassRuns;
    }
}
