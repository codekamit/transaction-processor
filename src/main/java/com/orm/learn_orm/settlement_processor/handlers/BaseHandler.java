package com.orm.learn_orm.settlement_processor.handlers;

@FunctionalInterface
public interface BaseHandler<T extends HandlerContext> {

    void process(T context);
}
