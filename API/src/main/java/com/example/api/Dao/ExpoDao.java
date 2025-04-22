package com.example.api.Dao;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.RowMapper.ExpoEditRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExpoDao {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExpoDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public ExpoEditDTO getExpoEditByID(int expoID) {
        System.out.println("ExpoDao: getExpoEditByAccount >> "+expoID);
        String sql = "SELECT name, avatar, price, introduction, openMode, openStatus, " +
                "openStart, openEnd, accessCode, maxParticipants, isShow";

        Map<String, Object> map = new HashMap<>();
        map.put("expoID", expoID);

        List<ExpoEditDTO> expoList = namedParameterJdbcTemplate.query(sql, map, new ExpoEditRowMapper());

        if(!expoList.isEmpty()) return expoList.get(0);
        else return null;
    }
}
