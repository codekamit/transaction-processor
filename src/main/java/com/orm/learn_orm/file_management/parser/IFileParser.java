package com.orm.learn_orm.file_management.parser;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileParser<T> {

    List<T> parseFile(MultipartFile file) throws IOException;
}
