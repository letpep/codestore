package com.letpep.osweb.IdGenerater.service.impl;


import com.letpep.osweb.IdGenerater.common.Constants;
import com.letpep.osweb.IdGenerater.dao.LetpepIdInfoDAO;
import com.letpep.osweb.IdGenerater.entity.LetpepIdInfo;
import com.letpep.osweb.IdGenerater.entity.SegmentId;
import com.letpep.osweb.IdGenerater.exception.LetpepIdSysException;
import com.letpep.osweb.IdGenerater.service.SegmentIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Date 2020
 */
@Component
public class DbSegmentIdServiceImpl implements SegmentIdService {

    private static final Logger logger = LoggerFactory.getLogger(DbSegmentIdServiceImpl.class);

    @Autowired
    private LetpepIdInfoDAO letpepIdInfoDAO;

    /**
     * Transactional标记保证query和update使用的是同一连接
     *
     * @param bizType
     * @return
     */
    @Override
    @Transactional
    public SegmentId getNextSegmentId(String bizType) {
        // 获取nextLetpepId的时候，有可能存在version冲突，需要重试
        for (int i = 0; i < Constants.RETRY; i++) {
            LetpepIdInfo letpepIdInfo = letpepIdInfoDAO.queryByBizType(bizType);
            if (letpepIdInfo == null) {
                throw new LetpepIdSysException("can not find biztype:" + bizType);
            }
            Long newMaxId = letpepIdInfo.getMaxId() + letpepIdInfo.getStep();
            Long oldMaxId = letpepIdInfo.getMaxId();
            int row = letpepIdInfoDAO.updateMaxId(letpepIdInfo.getId(), newMaxId, oldMaxId, letpepIdInfo.getVersion(),
                    letpepIdInfo.getBizType());
            if (row == 1) {
                letpepIdInfo.setMaxId(newMaxId);
                SegmentId segmentId = convert(letpepIdInfo);
                logger.info("getNextSegmentId success letpepIdInfo:{} current:{}", letpepIdInfo, segmentId);
                return segmentId;
            } else {
                logger.info("getNextSegmentId conflict letpepIdInfo:{}", letpepIdInfo);
            }
        }
        throw new LetpepIdSysException("get next segmentId conflict");
    }

    public SegmentId convert(LetpepIdInfo idInfo) {
        SegmentId segmentId = new SegmentId();
        segmentId.setCurrentId(new AtomicLong(idInfo.getMaxId() - idInfo.getStep()));
        segmentId.setMaxId(idInfo.getMaxId());
        segmentId.setRemainder(idInfo.getRemainder() == null ? 0 : idInfo.getRemainder());
        segmentId.setDelta(idInfo.getDelta() == null ? 1 : idInfo.getDelta());
        // 默认20%加载
        segmentId.setLoadingId(segmentId.getCurrentId().get() + idInfo.getStep() * Constants.LOADING_PERCENT / 100);
        return segmentId;
    }
}
