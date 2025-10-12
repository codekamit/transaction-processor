package com.orm.learn_orm.settlement_processor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface IPreprocessor {

    void preprocess(MultipartFile file) throws IOException;
}
