package com.letpep.osweb.IdGenerater.dao.impl;

import com.letpep.osweb.IdGenerater.dao.LetpepIdInfoDAO;
import com.letpep.osweb.IdGenerater.entity.LetpepIdInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Date 2020
 */
@Repository
public class LetpepIdInfoDAOImpl implements LetpepIdInfoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public LetpepIdInfo queryByBizType(String bizType) {
        String sql = "select id, biz_type, begin_id, max_id," +
                " step, delta, remainder, create_time, update_time, version" +
                " from tiny_id_info where biz_type = ?";
        List<LetpepIdInfo> list = jdbcTemplate.query(sql, new Object[]{bizType}, new TinyIdInfoRowMapper());
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public int updateMaxId(Long id, Long newMaxId, Long oldMaxId, Long version, String bizType) {
        String sql = "update tiny_id_info set max_id= ?," +
                " update_time=now(), version=version+1" +
                " where id=? and max_id=? and version=? and biz_type=?";
        return jdbcTemplate.update(sql, newMaxId, id, oldMaxId, version, bizType);
    }


    public static class TinyIdInfoRowMapper implements RowMapper<LetpepIdInfo> {

        @Override
        public LetpepIdInfo mapRow(ResultSet resultSet, int i) throws SQLException {
            LetpepIdInfo letpepIdInfo = new LetpepIdInfo();
            letpepIdInfo.setId(resultSet.getLong("id"));
            letpepIdInfo.setBizType(resultSet.getString("biz_type"));
            letpepIdInfo.setBeginId(resultSet.getLong("begin_id"));
            letpepIdInfo.setMaxId(resultSet.getLong("max_id"));
            letpepIdInfo.setStep(resultSet.getInt("step"));
            letpepIdInfo.setDelta(resultSet.getInt("delta"));
            letpepIdInfo.setRemainder(resultSet.getInt("remainder"));
            letpepIdInfo.setCreateTime(resultSet.getDate("create_time"));
            letpepIdInfo.setUpdateTime(resultSet.getDate("update_time"));
            letpepIdInfo.setVersion(resultSet.getLong("version"));
            return letpepIdInfo;
        }
    }
}
