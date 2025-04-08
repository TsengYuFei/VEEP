package com.example.api.Dao;

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

    public User getBriefUserByAccount(String account){
        System.out.println("UserDao: getBriefUserByAccount >> "+account);
        String sql = "SELECT name, userAccount, avatar, bio, background, followers, following, role FROM user WHERE userAccount = :account";

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if(!userList.isEmpty()) {
            return userList.get(0);
        } else return null;
    }
}
