package com.letpep.osweb.IdGenerater.dao;


import com.letpep.osweb.IdGenerater.entity.LetpepIdInfo;

/**
 * Date 2020
 */
public interface LetpepIdInfoDAO {
    /**
     * 根据bizType获取db中的letpepId对象
     * @param bizType
     * @return
     */
    LetpepIdInfo queryByBizType(String bizType);

    /**
     * 根据id、oldMaxId、version、bizType更新最新的maxId
     * @param id
     * @param newMaxId
     * @param oldMaxId
     * @param version
     * @param bizType
     * @return
     */
    int updateMaxId(Long id, Long newMaxId, Long oldMaxId, Long version, String bizType);
}
