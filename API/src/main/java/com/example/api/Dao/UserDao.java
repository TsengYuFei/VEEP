package com.example.api.Dao;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.DTO.UserOverviewDTO;
import com.example.api.Request.UserRequest;
import com.example.api.RowMapper.UserDetailRowMapper;
import com.example.api.RowMapper.UserOverviewRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

    public String createUser(UserRequest userRequest){
        System.out.println("UserDao: createUser");
        String sql = "INSERT INTO user (name, userAccount, password, tel, mail, avatar, birthday) " +
                "VALUES (:name, :userAccount, :password, :tel, :mail, :avatar, :birthday)";

        Map<String, Object> map = new HashMap<>();
        map.put("name", userRequest.getName());
        map.put("userAccount", userRequest.getUserAccount());
        map.put("password", userRequest.getPassword());
        map.put("tel", userRequest.getTel());
        map.put("mail", userRequest.getMail());
        map.put("avatar", userRequest.getAvatar());
        map.put("birthday", userRequest.getBirthday());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));

        return map.get("userAccount").toString();
    }
}