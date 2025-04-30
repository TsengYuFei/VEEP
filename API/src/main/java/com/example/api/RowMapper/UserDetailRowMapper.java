package com.example.api.RowMapper;

import com.example.api.DTO.Response.UserDetailResponse;
import com.example.api.Entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetailRowMapper implements RowMapper<UserDetailResponse> {

    @Override
    public UserDetailResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDetailResponse user = new UserDetailResponse();
        Role role = Role.valueOf(rs.getString("role"));

        user.setName(rs.getString("name"));
        user.setUserAccount(rs.getString("userAccount"));
        user.setAvatar(rs.getString("avatar"));
        user.setBio(rs.getString("bio"));
        user.setBackground(rs.getString("background"));
        user.setShowFollowers(rs.getBoolean("showFollowers"));
        user.setShowFollowing(rs.getBoolean("showFollowing"));
        user.setShowHistory(rs.getBoolean("showHistory"));
        user.setShowCurrentExpo(rs.getBoolean("showCurrentExpo"));
        user.setShowCurrentBooth(rs.getBoolean("showCurrentBooth"));
        user.setRole(role);
        return user;
    }
}
