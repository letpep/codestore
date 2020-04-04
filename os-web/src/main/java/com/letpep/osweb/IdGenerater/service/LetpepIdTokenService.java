package com.letpep.osweb.IdGenerater.service;

/**
 * Date 2020
 */
public interface LetpepIdTokenService {
    /**
     * 是否有权限
     * @param bizType
     * @param token
     * @return
     */
    boolean canVisit(String bizType, String token);
}
