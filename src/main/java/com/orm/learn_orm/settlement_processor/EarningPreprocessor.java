package com.orm.learn_orm.settlement_processor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("earningPreprocessor")
public class EarningPreprocessor implements IPreprocessor {

    @Override
    public void preprocess(MultipartFile file) {

    }
}
