package com.letpep.osweb.IdGenerater.factory;


import com.letpep.osweb.IdGenerater.generator.IdGenerator;

/**
 * Date 2020
 */
public interface IdGeneratorFactory {
    /**
     * 根据bizType创建id生成器
     * @param bizType
     * @return
     */
    IdGenerator getIdGenerator(String bizType);
}
