package com.letpep.osweb.IdGenerater.factory.impl;

import com.letpep.osweb.IdGenerater.factory.AbstractIdGeneratorFactory;
import com.letpep.osweb.IdGenerater.generator.IdGenerator;
import com.letpep.osweb.IdGenerater.generator.impl.CachedIdGenerator;
import com.letpep.osweb.IdGenerater.service.SegmentIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date 2020
 */
@Component
public class IdGeneratorFactoryServer extends AbstractIdGeneratorFactory {

    private static final Logger logger = LoggerFactory.getLogger(CachedIdGenerator.class);
    @Autowired
    private SegmentIdService letpepIdService;

    @Override
    public IdGenerator createIdGenerator(String bizType) {
        logger.info("createIdGenerator :{}", bizType);
        return new CachedIdGenerator(bizType, letpepIdService);
    }
}
