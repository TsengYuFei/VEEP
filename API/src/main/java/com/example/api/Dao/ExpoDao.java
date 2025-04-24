package com.example.api.Dao;

import com.example.api.DTO.ExpoEditDTO;
import com.example.api.Request.ExpoRequest;
import com.example.api.RowMapper.ExpoEditRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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


    public ExpoEditDTO getExpoEditByID(Integer expoID) {
        System.out.println("ExpoDao: getExpoEditByAccount >> "+expoID);
        String sql = "SELECT name, avatar, price, introduction, openMode, openStatus, " +
                "openStart, openEnd, accessCode, maxParticipants, display " +
                "FROM expo " +
                "WHERE expoID = :expoID";

        Map<String, Object> map = new HashMap<>();
        map.put("expoID", expoID);

        List<ExpoEditDTO> expoList = namedParameterJdbcTemplate.query(sql, map, new ExpoEditRowMapper());

        if(!expoList.isEmpty()) return expoList.get(0);
        else return null;
    }


    public Integer createExpo (ExpoRequest request){
        System.out.println("ExpoDao: createExpo");
        String sql = "INSERT INTO expo (name, avatar, price, introduction, openMode, " +
                "openStatus, openStart, openEnd, accessCode, maxParticipants, display)" +
                "VALUES (:name, :avatar, :price, :introduction, :openMode,  :openStatus, " +
                ":openStart, :openEnd, :accessCode, :maxParticipants, :display)";

        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getName());
        map.put("avatar", request.getAvatar());
        map.put("price", request.getPrice());
        map.put("introduction", request.getIntroduction());
        map.put("openMode", request.getOpenMode().toString());
        map.put("openStatus", request.getOpenStatus());
        map.put("openStart", request.getOpenStart());
        map.put("openEnd", request.getOpenEnd());
        map.put("accessCode", request.getAccessCode());
        map.put("maxParticipants", request.getMaxParticipants());
        map.put("display", request.getDisplay());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer expoID = keyHolder.getKey().intValue();

        return expoID;
    }


    public void updateExpoByID(Integer expoID, ExpoRequest request){
        System.out.println("ExpoDao: updateExpoByID  >> "+expoID);
        String sql = "UPDATE expo SET name = :name, avatar = :avatar, price = :price, " +
                "introduction = :introduction, openMode = :openMode, openStatus = :openStatus, " +
                "openStart = :openStart, openEnd = :openEnd, accessCode = :accessCode, " +
                "maxParticipants = :maxParticipants, display = :display " +
                "WHERE expoID = :expoID";

        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getName());
        map.put("avatar", request.getAvatar());
        map.put("price", request.getPrice());
        map.put("introduction", request.getIntroduction());
        map.put("openMode", request.getOpenMode().toString());
        map.put("openStatus", request.getOpenStatus());
        map.put("openStart", request.getOpenStart());
        map.put("openEnd", request.getOpenEnd());
        map.put("accessCode", request.getAccessCode());
        map.put("maxParticipants", request.getMaxParticipants());
        map.put("display", request.getDisplay());
        map.put("expoID", expoID);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
