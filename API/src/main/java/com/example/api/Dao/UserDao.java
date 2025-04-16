package com.example.api.Dao;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.DTO.UserOverviewDTO;
import com.example.api.RowMapper.UserDetailRowMapper;
import com.example.api.RowMapper.UserOverviewRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public UserDetailDTO getUserDetailByAccount(String account){
        System.out.println("UserDao: getUserDetailByAccount >> "+account);
        String sql = "SELECT name, userAccount, avatar, bio, background," +
                "showFollowers, showFollowing, showHistory, showCurrentExpo, showCurrentBooth, role " +
                "FROM user " +
                "WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<UserDetailDTO> userList = namedParameterJdbcTemplate.query(sql, map, new UserDetailRowMapper());

        if(!userList.isEmpty()) {
            return userList.get(0);
        } else return null;
    }

    public UserOverviewDTO getUserOverviewByAccount(String account){
        System.out.println("UserDao: getUserOverviewByAccount >> "+account);
        String sql = "SELECT name, userAccount, avatar, role FROM user WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<UserOverviewDTO> userList = namedParameterJdbcTemplate.query(sql, map, new UserOverviewRowMapper());

        if(!userList.isEmpty()) {
            return userList.get(0);
        } else return null;
    }
}