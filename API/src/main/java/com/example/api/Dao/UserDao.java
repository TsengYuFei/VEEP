package com.example.api.Dao;

import com.example.api.DTO.UserDetailDTO;
import com.example.api.Model.User;
import com.example.api.RowMapper.UserRowMapper;
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

    public UserDetailDTO getDetailUserByAccount(String account){
        System.out.println("UserDao: getDetailUserByAccount >> "+account);
        String sql = "SELECT name, userAccount, avatar, bio, background, followers," +
                "following, showFollowers, showFollowing, showHistory, showCurrentExpo, showCurrentBooth, role " +
                "FROM user " +
                "WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<UserDetailDTO> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(!userList.isEmpty()) {
            return userList.get(0);
        } else return null;
    }
}
