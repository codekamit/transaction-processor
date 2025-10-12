package com.orm.learn_orm.file_management.processor;


import com.orm.learn_orm.file_management.header.IFileHeader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component("csvFileProcessor")
public class CSVFileProcessor implements IFileProcessor {

    @Override
    public List<List<String>> processFile(MultipartFile file, IFileHeader fileHeaders) throws IOException {
        List<List<String>> records = new ArrayList<>();
        var inputStream = file.getInputStream();
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> csvRecords = csvParser.getRecords();

        for(int idx = fileHeaders.headerStartCol(); idx < csvRecords.size(); idx++) {
            List<String> row = new ArrayList<>();
            csvRecords.get(idx).forEach(csvRecord -> row.add(csvRecord.trim()));
            records.add(row);
        }

        return records;
    }
}