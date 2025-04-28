package com.example.api.Dao;

import com.example.api.DTO.BoothEditDTO;
import com.example.api.Request.BoothRequest;
import com.example.api.RowMapper.BoothEditRowMapper;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.coyote.Request;
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
public class BoothDao {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BoothDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public BoothEditDTO getBoothByID(Integer boothID){
        System.out.println("BoothDao: getBoothByID >> "+boothID);
        String sql = "SELECT name, avatar, introduction, openMode, " +
                "openStatus, openStart, openEnd, maxParticipants, display " +
                "FROM booth " +
                "WHERE boothID = :boothID";

        Map<String, Object> map = new HashMap<>();
        map.put("boothID", boothID);

        List<BoothEditDTO> boothList = namedParameterJdbcTemplate.query(sql, map, new BoothEditRowMapper());

        if(!boothList.isEmpty()) return boothList.get(0);
        else return null;
    }


    public Integer createBooth(BoothRequest request){
        System.out.println("BoothDao: createBooth");
        String sql = "INSERT INTO booth(name, avatar, introduction, openMode, " +
                "openStatus, openStart, openEnd, maxParticipants, display) " +
                "VALUES (:name, :avatar, :introduction, :openMode, :openStatus, " +
                ":openStart, :openEnd, :maxParticipants, :display)";

        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getName());
        map.put("avatar", request.getAvatar());
        map.put("introduction", request.getIntroduction());
        map.put("openMode", request.getOpenMode().toString());
        map.put("openStatus", request.getOpenStatus());
        map.put("openStart", request.getOpenStart());
        map.put("openEnd", request.getOpenEnd());
        map.put("maxParticipants", request.getMaxParticipants());
        map.put("display", request.getDisplay());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();
    }


    public void updateBoothByID(Integer boothID, BoothRequest request){
        System.out.println("BoothDao: updateBoothByID >> "+boothID);
        String sql = "UPDATE booth SET name = :name, avatar = :avatar, introduction = :introduction, " +
                "openMode = :openMode, openStatus = :openStatus, openStart = :openStart, openEnd = :openEnd, " +
                "maxParticipants = :maxParticipants, display = :display " +
                "WHERE boothID = :boothID";

        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getName());
        map.put("avatar", request.getAvatar());
        map.put("introduction", request.getIntroduction());
        map.put("openMode", request.getOpenMode().toString());
        map.put("openStatus", request.getOpenStatus());
        map.put("openStart", request.getOpenStart());
        map.put("openEnd", request.getOpenEnd());
        map.put("maxParticipants", request.getMaxParticipants());
        map.put("display", request.getDisplay());
        map.put("boothID", boothID);

        namedParameterJdbcTemplate.update(sql, map);
    }


    public void deleteBoothByID(Integer boothID){
        System.out.println("BoothDao: deleteBoothByID >> "+boothID);
        String sql = "DELETE FROM booth WHERE boothID = :boothID";

        Map<String, Object> map = new HashMap<>();
        map.put("boothID", boothID);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
