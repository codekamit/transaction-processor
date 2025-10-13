package com.orm.learn_orm.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import com.fasterxml.uuid.Generators;

public class UuidV7Generator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return Generators.timeBasedEpochGenerator().generate();
    }
}