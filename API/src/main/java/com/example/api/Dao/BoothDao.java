package com.example.api.Dao;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.RowMapper.BoothEditRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BoothDao {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BoothDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public BoothEditDTO getBoothByID(Integer boothID){
        System.out.println("BoothDao: getBoothByID >> "+boothID);
        String sql = "SELECT name, avatar, introduction, openMode, " +
                "openStatus, openStart, openEnd, maxParticipants, isShow" +
                "FROM booth " +
                "WHERE boothID = :boothID";

        Map<String, Object> map = new HashMap<>();
        map.put("boothID", boothID);

        List<BoothEditDTO> boothList = namedParameterJdbcTemplate.query(sql, map, new BoothEditRowMapper());

        if(!boothList.isEmpty()) return boothList.get(0);
        else return null;
    }
}
