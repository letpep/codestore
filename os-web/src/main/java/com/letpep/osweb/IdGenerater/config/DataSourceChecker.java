package com.letpep.osweb.IdGenerater.config;

import com.letpep.osweb.IdGenerater.dao.LetpepIdTokenDAO;
import com.letpep.osweb.IdGenerater.entity.LetpepIdToken;
import com.letpep.osweb.IdGenerater.service.LetpepIdTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright (C), 2020-04-05
 * FileName: DataSourceCheckConfig
 * Author:   lx
 * Date:     2020/4/5 3:07 PM
 * Description: 连接池检测
 */
@Component
public class DataSourceChecker {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceChecker.class);
    private  volatile List<String> sourcekeys= new CopyOnWriteArrayList<>();
    private  volatile  List<String> sourceCheckkeys= new CopyOnWriteArrayList<>();
    private  volatile  String currentCheckSourcekey = "";
    private  Boolean isChecking =false;
    private  volatile  String checkingSourceKey;
    @Autowired
    private LetpepIdTokenDAO letpepIdTokenDAO;


    public List<String> getSourcekeys() {
        return sourcekeys;
    }

    public void setSourcekeys(List<String> sourcekeys) {
        this.sourcekeys = sourcekeys;
    }

    public List<String> getSourceCheckkeys() {
        return sourceCheckkeys;
    }

    public void setSourceCheckkeys(List<String> sourceCheckkeys) {
        this.sourceCheckkeys = sourceCheckkeys;
    }

    /**
     * 1分钟刷新一次token
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public synchronized void checking() {
        this.isChecking =true;
        this.sourceCheckkeys.clear();
        for(String checkkey :this.sourcekeys){
            int j =1;
            checkingSourceKey = checkkey;
            while (true){
                if(j>3) {
                    break;
                }
                try{
                    letpepIdTokenDAO.selectAll();
                    this.sourceCheckkeys.add(checkkey);
                    break;
                }catch (DataAccessException e){
                    logger.error("获取连接-"+checkingSourceKey+"—失败"+j+"次",e);
                    j++;
                    continue;
                }

            }
        }

        this.isChecking =false;
    }
    public  String getDateSource(List<String> dataSourceKeys){
        if(this.isChecking){
            System.out.println("检测获取key中");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("检测获取key结束");
            return this.checkingSourceKey;
        }
        else {
            System.out.println("正常获取key中");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("正常获取key结束");
            if (this.sourceCheckkeys.size() == 0) {
                if(dataSourceKeys.size() == 1) {
                    return dataSourceKeys.get(0);
                }
                Random r = new Random();
                return dataSourceKeys.get(r.nextInt(dataSourceKeys.size()));
            }

            if(sourceCheckkeys.size() == 1) {
                return sourceCheckkeys.get(0);
            }
            Random r = new Random();
            return sourceCheckkeys.get(r.nextInt(sourceCheckkeys.size()));
            }




    }


}
