package com.example.api.RowMapper;

import com.example.api.DTO.Response.UserOverviewResponse;
import com.example.api.Entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserOverviewRowMapper implements RowMapper<UserOverviewResponse> {

    @Override
    public UserOverviewResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserOverviewResponse user = new UserOverviewResponse();
        Role role = Role.valueOf(rs.getString("role"));

        user.setName(rs.getString("name"));
        user.setUserAccount(rs.getString("userAccount"));
        user.setAvatar(rs.getString("avatar"));
        user.setRole(role);
        return user;
    }
}
