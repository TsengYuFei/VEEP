package com.example.api.Repository;

import com.example.api.DTO.Response.BoothEditResponse;
import com.example.api.DTO.Request.BoothCreateRequest;
import com.example.api.RowMapper.BoothEditRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class BoothRepository {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BoothRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public BoothEditResponse getBoothByID(Integer boothID){
        System.out.println("BoothDao: getBoothByID >> "+boothID);
        String sql = "SELECT name, avatar, introduction, openMode, " +
                "openStatus, openStart, openEnd, maxParticipants, display " +
                "FROM booth " +
                "WHERE boothID = :boothID";

        Map<String, Object> map = new HashMap<>();
        map.put("boothID", boothID);

        List<BoothEditResponse> boothList = namedParameterJdbcTemplate.query(sql, map, new BoothEditRowMapper());

        if(!boothList.isEmpty()) return boothList.get(0);
        else return null;
    }


    public Integer createBooth(BoothCreateRequest request){
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

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }


    public void updateBoothByID(Integer boothID, BoothCreateRequest request){
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
