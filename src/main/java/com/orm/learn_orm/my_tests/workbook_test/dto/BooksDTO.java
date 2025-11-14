package com.orm.learn_orm.my_tests.workbook_test.dto;


import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportColumn;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PartialExport;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PerfectExport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksDTO implements IExportable {
    @ExportColumn(name="Name", order = 4)
    private String bookName;
    @ExportColumn(name="Publication", order = 3)
    private String publication;
    @ExportColumn(name="Publication Date", order = 2, groups = {PerfectExport.class})
    private LocalDate publicationDate;
    @ExportColumn(name="Genre", order = 1, groups = {PartialExport.class})
    private String genre;
}
