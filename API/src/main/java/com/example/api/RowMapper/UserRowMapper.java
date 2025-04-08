package com.example.api.RowMapper;

import com.example.api.Model.Role;
import com.example.api.Model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setName(rs.getString("name"));
        user.setUserAccount(rs.getString("userAccount"));
        user.setAvatar(rs.getString("avatar"));
        user.setBio(rs.getString("bio"));
        user.setBackground(rs.getString("background"));
        user.setFollowers(rs.getInt("followers"));
        user.setFollowing(rs.getInt("following"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
    }
}
