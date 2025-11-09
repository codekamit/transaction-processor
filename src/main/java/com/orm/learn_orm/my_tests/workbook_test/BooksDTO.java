package com.orm.learn_orm.my_tests.workbook_test;


import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.ExportColumn;
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
    @ExportColumn(name="Name")
    private String bookName;
    @ExportColumn(name="Publication")
    private String publication;
    @ExportColumn(name="Publication Date")
    private LocalDate publicationDate;
    @ExportColumn(name="Genre")
    private String genre;
}
