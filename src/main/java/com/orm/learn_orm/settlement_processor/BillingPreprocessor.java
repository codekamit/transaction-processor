package com.orm.learn_orm.settlement_processor;

import com.orm.learn_orm.factory.AgencyFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component("billingPreprocessor")
public class BillingPreprocessor implements IPreprocessor {

    @Override
    public void preprocess(MultipartFile file, AgencyFactory<?> agencyFactory) {

    }
}