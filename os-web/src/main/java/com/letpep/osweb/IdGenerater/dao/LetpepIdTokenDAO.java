package com.letpep.osweb.IdGenerater.dao;


import com.letpep.osweb.IdGenerater.entity.LetpepIdToken;

import java.util.List;

/**
 * Date 2020
 */
public interface LetpepIdTokenDAO {
    /**
     * 查询db中所有的token信息
     * @return
     */
    List<LetpepIdToken> selectAll();
}
