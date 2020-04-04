package com.letpep.osweb.IdGenerater.generator;

import java.util.List;

/**
 * Date 2020
 */
public interface IdGenerator {
    /**
     * get next id
     * @return
     */
    Long nextId();

    /**
     * get next id batch
     * @param batchSize
     * @return
     */
    List<Long> nextId(Integer batchSize);
}
