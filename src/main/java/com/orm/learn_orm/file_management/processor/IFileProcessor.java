package com.orm.learn_orm.file_management.processor;

import com.orm.learn_orm.file_management.header.IFileHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileProcessor {

    List<List<String>> processFile(MultipartFile file, IFileHeader fileHeaders) throws IOException;
}
