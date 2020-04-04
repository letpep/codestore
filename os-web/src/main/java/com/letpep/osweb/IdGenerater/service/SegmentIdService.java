package com.letpep.osweb.IdGenerater.service;


import com.letpep.osweb.IdGenerater.entity.SegmentId;

/**
 * Date 2020
 */
public interface SegmentIdService {

    /**
     * 根据bizType获取下一个SegmentId对象
     * @param bizType
     * @return
     */
    SegmentId getNextSegmentId(String bizType);

}
