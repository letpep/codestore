package com.letpep.osweb.IdGenerater.dao.impl;

import com.letpep.osweb.IdGenerater.dao.LetpepIdTokenDAO;
import com.letpep.osweb.IdGenerater.entity.LetpepIdToken;
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
public class LetpepIdTokenDAOImpl implements LetpepIdTokenDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<LetpepIdToken> selectAll() {
        String sql = "select id, token, biz_type, remark, " +
                "create_time, update_time from letpep_id_token";
        return jdbcTemplate.query(sql, new TinyIdTokenRowMapper());
    }

    public static class TinyIdTokenRowMapper implements RowMapper<LetpepIdToken> {

        @Override
        public LetpepIdToken mapRow(ResultSet resultSet, int i) throws SQLException {
            LetpepIdToken token = new LetpepIdToken();
            token.setId(resultSet.getInt("id"));
            token.setToken(resultSet.getString("token"));
            token.setBizType(resultSet.getString("biz_type"));
            token.setRemark(resultSet.getString("remark"));
            token.setCreateTime(resultSet.getDate("create_time"));
            token.setUpdateTime(resultSet.getDate("update_time"));
            return token;
        }
    }
}
