package com.letpep.osweb.IdGenerater.service.impl;


import com.letpep.osweb.IdGenerater.dao.LetpepIdTokenDAO;
import com.letpep.osweb.IdGenerater.entity.LetpepIdToken;
import com.letpep.osweb.IdGenerater.service.LetpepIdTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Date 2020
 */
@Component
public class LetpepIdTokenServiceImpl implements LetpepIdTokenService {

    @Autowired
    private LetpepIdTokenDAO tinyIdTokenDAO;

    private static Map<String, Set<String>> token2bizTypes = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(LetpepIdTokenServiceImpl.class);

    public List<LetpepIdToken> queryAll() {
        return tinyIdTokenDAO.selectAll();
    }

    /**
     * 1分钟刷新一次token
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void refresh() {
        logger.info("refresh token begin");
        init();
    }

    @PostConstruct
    private synchronized void init() {
        logger.info("letpepId token init begin");
        List<LetpepIdToken> list = queryAll();
        Map<String, Set<String>> map = converToMap(list);
        token2bizTypes = map;
        logger.info("letpepId token init success, token size:{}", list == null ? 0 : list.size());
    }

    @Override
    public boolean canVisit(String bizType, String token) {
        if (StringUtils.isEmpty(bizType) || StringUtils.isEmpty(token)) {
            return false;
        }
        Set<String> bizTypes = token2bizTypes.get(token);
        return (bizTypes != null && bizTypes.contains(bizType));
    }

    public Map<String, Set<String>> converToMap(List<LetpepIdToken> list) {
        Map<String, Set<String>> map = new HashMap<>(64);
        if (list != null) {
            for (LetpepIdToken tinyIdToken : list) {
                if (!map.containsKey(tinyIdToken.getToken())) {
                    map.put(tinyIdToken.getToken(), new HashSet<String>());
                }
                map.get(tinyIdToken.getToken()).add(tinyIdToken.getBizType());
            }
        }
        return map;
    }

}
