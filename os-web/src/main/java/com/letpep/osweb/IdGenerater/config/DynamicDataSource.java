package com.letpep.osweb.IdGenerater.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Date 2020
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private List<String> dataSourceKeys;
    @Autowired
    private DataSourceChecker dataSourceChecker;

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceChecker.getDateSource(dataSourceKeys);
    }

    public List<String> getDataSourceKeys() {
        return dataSourceKeys;
    }

    public void setDataSourceKeys(List<String> dataSourceKeys) {
        this.dataSourceKeys = dataSourceKeys;
    }
}
